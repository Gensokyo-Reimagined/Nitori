// Nitori Copyright (C) 2024 Gensokyo Reimagined
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
package net.gensokyoreimagined.nitori.core;

import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

/*
 * Originally from CaffeineMC, licensed under GNU Lesser General Public License v3.0
 * See https://github.com/CaffeineMC/lithium-fabric for more information/sources
 */

@Mixin(Mth.class)
public class MixinMth {
    @Unique
    private static final int[] SINE_TABLE_INT = new int[16384 + 1];
    @Unique
    private static final float SINE_TABLE_MIDPOINT;

    static {
        final float[] SINE_TABLE = MixinMth.SIN();
        // Copy the sine table, covering to raw int bits
        for (int i = 0; i < SINE_TABLE_INT.length; i++) {
            SINE_TABLE_INT[i] = Float.floatToRawIntBits(SINE_TABLE[i]);
        }

        SINE_TABLE_MIDPOINT = SINE_TABLE[SINE_TABLE.length / 2];

        // Test that the lookup table is correct during runtime
        for (int i = 0; i < SINE_TABLE.length; i++) {
            float expected = SINE_TABLE[i];
            float value = gensouHacks$lookup(i);

            if (expected != value) {
                throw new IllegalArgumentException(String.format("LUT error at index %d (expected: %s, found: %s)", i, expected, value));
            }
        }
    }

    @Unique
    private static float gensouHacks$lookup(int index) {
        // A special case... Is there some way to eliminate this?
        if (index == 32768) {
            return SINE_TABLE_MIDPOINT;
        }

        // Trigonometric identity: sin(-x) = -sin(x)
        // Given a domain of 0 <= x <= 2*pi, just negate the value if x > pi.
        // This allows the sin table size to be halved.
        int neg = (index & 0x8000) << 16;

        // All bits set if (pi/2 <= x), none set otherwise
        // Extracts the 15th bit from 'half'
        int mask = (index << 17) >> 31;

        // Trigonometric identity: sin(x) = sin(pi/2 - x)
        int pos = (0x8001 & mask) + (index ^ mask);

        // Wrap the position in the table. Moving this down to immediately before the array access
        // seems to help the Hotspot compiler optimize the bit math better.
        pos &= 0x7fff;

        // Fetch the corresponding value from the LUT and invert the sign bit as needed
        // This directly manipulate the sign bit on the float bits to simplify logic
        return Float.intBitsToFloat(SINE_TABLE_INT[pos] ^ neg);
    }

    @Accessor("SIN")
    public static float[] SIN() {
        throw new AssertionError("Mixin failed to apply!");
    }

    /**
     * @author DoggySazHi
     * @reason Implementation of 0007-lithium-CompactSineLUT.patch
     */
    @Overwrite
    public static float sin(float angle) {
        return gensouHacks$lookup((int) (angle * 10430.378f) & 0xFFFF);
    }

    /**
     * @author DoggySazHi
     * @reason Implementation of 0007-lithium-CompactSineLUT.patch
     */
    @Overwrite
    public static float cos(float angle) {
        return gensouHacks$lookup((int) (angle * 10430.378f + 16384.0f) & 0xFFFF);
    }
}
