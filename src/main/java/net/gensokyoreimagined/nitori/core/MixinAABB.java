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

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AABB.class)
public class MixinAABB {
    @Shadow @Final public double minX;
    @Shadow @Final public double minY;
    @Shadow @Final public double minZ;
    @Shadow @Final public double maxX;
    @Shadow @Final public double maxY;
    @Shadow @Final public double maxZ;

    /*
     * Note: did not impelment the static constructor in the original patch, as it is not needed for the patch to work
     */

    /**
     * @author DoggySazHi
     * @reason Implementation of 0005-lithium-fast-util.patch, requires a overwrite to avoid calling `axis.choose`
     */
    @Overwrite
    public double min(Direction.Axis axis) {
        return switch (axis.ordinal()) {
            case 0 -> // X
                    this.minX;
            case 1 -> // Y
                    this.minY;
            case 2 -> // Z
                    this.minZ;
            default -> throw new IllegalArgumentException();
        };
    }

    /**
     * @author DoggySazHi
     * @reason Implementation of 0005-lithium-fast-util.patch, requires a overwrite to avoid calling `axis.choose`
     */
    @Overwrite
    public double max(Direction.Axis axis) {
        return switch (axis.ordinal()) {
            case 0 -> // X
                    this.maxX;
            case 1 -> // Y
                    this.maxY;
            case 2 -> // Z
                    this.maxZ;
            default -> throw new IllegalArgumentException();
        };
    }
}
