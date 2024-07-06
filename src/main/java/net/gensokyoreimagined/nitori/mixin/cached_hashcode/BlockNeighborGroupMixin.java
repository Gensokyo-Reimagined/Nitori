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
package net.gensokyoreimagined.nitori.mixin.cached_hashcode;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.BlockStatePairKey.class)
public class BlockNeighborGroupMixin {
    @Shadow
    @Final
    private BlockState first;

    @Shadow
    @Final
    private BlockState second;

    @Shadow
    @Final
    private Direction direction;

    private int hash;

    /**
     * @reason Initialize the object's hashcode and cache it
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void generateHash(BlockState blockState_1, BlockState blockState_2, Direction direction_1, CallbackInfo ci) {
        int hash = this.first.hashCode();
        hash = 31 * hash + this.second.hashCode();
        hash = 31 * hash + this.direction.hashCode();

        this.hash = hash;
    }

    /**
     * @reason Uses the cached hashcode
     * @author JellySquid
     */
    @Overwrite(remap = false)
    public int hashCode() {
        return this.hash;
    }
}