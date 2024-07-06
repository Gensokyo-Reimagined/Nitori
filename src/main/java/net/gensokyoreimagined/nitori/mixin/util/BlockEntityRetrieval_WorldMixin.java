package net.gensokyoreimagined.nitori.mixin.util;

import net.gensokyoreimagined.nitori.common.world.blockentity.BlockEntityGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.Containers;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(Level.class)
public abstract class BlockEntityRetrieval_WorldMixin implements BlockEntityGetter, Containers {
    @Final
    public boolean isClient;

    @Final
    private Thread thread;

    public abstract LevelChunk getChunk(int i, int j);

    @Nullable
    public abstract ChunkPos getChunk(int chunkX, int chunkZ, ChunkAccess leastStatus, boolean create);

    @Override
    public BlockEntity lithium$getLoadedExistingBlockEntity(BlockPos pos) {
        if (!this.isOutOfHeightLimit(pos)) {
            if (this.isClient || Thread.currentThread() == this.thread) {
                ChunkPos chunk = this.getChunk(SectionPos.posToSectionCoord(pos.getX()), SectionPos.posToSectionCoord(pos.getZ()), ChunkAccess.FULL, false);
                if (chunk != null) {
                    return chunk.BlockEntityType.getBlockEntity(pos);
                }
            }
        }
        return null;
    }
}