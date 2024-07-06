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

import net.gensokyoreimagined.nitori.common.world.ChunkRandomSource;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Taken from Lithium
// https://github.com/CaffeineMC/lithium-fabric/blob/427dd75ffc922cc1858c1db4b283cc54744567e0/src/main/java/me/jellysquid/mods/lithium/mixin/alloc/chunk_random/ServerWorldMixin.java#L24

@Mixin(ServerLevel.class)
public abstract class MixinServerWorld {
    @Unique
    private final BlockPos.MutableBlockPos nitori$randomPosInChunkCachedPos = new BlockPos.MutableBlockPos();

    /**
     * @reason Avoid allocating BlockPos every invocation through using our allocation-free variant
     */
    @Redirect(
            method = "tickChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;getBlockRandomPos(IIII)Lnet/minecraft/core/BlockPos;"
            )
    )
    private BlockPos redirectTickGetRandomPosInChunk(ServerLevel serverWorld, int x, int y, int z, int mask) {
        ((ChunkRandomSource) serverWorld).nitori$getRandomPosInChunk(x, y, z, mask, this.nitori$randomPosInChunkCachedPos);

        return this.nitori$randomPosInChunkCachedPos;
    }

    /**
     * @reason Ensure an immutable block position is passed on block tick
     */
    @Redirect(
            method = "tickChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;randomTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V"
            )
    )
    private void redirectBlockStateTick(BlockState blockState, ServerLevel world, BlockPos pos, RandomSource rand) {
        blockState.randomTick(world, pos.immutable(), rand);
    }
}