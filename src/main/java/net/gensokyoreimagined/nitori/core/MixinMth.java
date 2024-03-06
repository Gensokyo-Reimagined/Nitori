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

import net.gensokyoreimagined.nitori.common.util.math.CompactSineLUT;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Mth.class)
public class MixinMth {
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
        return CompactSineLUT.sin(angle);
    }

    /**
     * @author DoggySazHi
     * @reason Implementation of 0007-lithium-CompactSineLUT.patch
     */
    @Overwrite
    public static float cos(float angle) {
        return CompactSineLUT.cos(angle);
    }
}
