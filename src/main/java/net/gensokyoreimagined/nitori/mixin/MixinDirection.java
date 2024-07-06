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
package net.gensokyoreimagined.nitori.mixin;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Direction.class)
public class MixinDirection {
    @Shadow @Final private static Direction[] VALUES;
    @Shadow @Final private int oppositeIndex;

    /**
     * @author DoggySazHi
     * @reason Implementation of 0005-lithium-fast-util.patch, requires a overwrite to avoid calling `from3DDataValue`
     */
    @Overwrite
    public Direction getOpposite() {
        return VALUES[this.oppositeIndex];
    }

    /**
     * @author DoggySazHi
     * @reason Implementation of 0005-lithium-fast-util.patch, requires a overwrite to avoid calling `Util.getRandom`
     */
    @Overwrite
    public static Direction getRandom(RandomSource random) {
        return VALUES[random.nextInt(VALUES.length)];
    }
}
