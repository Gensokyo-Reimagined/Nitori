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
package net.gensokyoreimagined.nitori.mixin.util;

import net.gensokyoreimagined.nitori.common.world.blockentity.BlockEntityGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(Level.class)
public abstract class MixinLevelBlockEntityRetrieval implements BlockEntityGetter, LevelAccessor {
    @Shadow
    @Final
    public boolean isClientSide;

    @Shadow
    @Final
    public Thread thread;

    @Shadow
    public abstract LevelChunk getChunk(int i, int j);

    @Shadow
    @Nullable
    public abstract ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus leastStatus, boolean create);

    @Override
    public BlockEntity lithium$getLoadedExistingBlockEntity(BlockPos pos) {
        if (!this.isOutsideBuildHeight(pos)) {
            if (this.isClientSide || Thread.currentThread() == this.thread) {
                ChunkAccess chunk = this.getChunk(SectionPos.posToSectionCoord(pos.getX()), SectionPos.posToSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
                if (chunk != null) {
                    return chunk.getBlockEntity(pos);
                }
            }
        }
        return null;
    }
}