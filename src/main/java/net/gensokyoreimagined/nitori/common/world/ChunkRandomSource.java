package net.gensokyoreimagined.nitori.common.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.Level;

public interface ChunkRandomSource {

    void lithium$getRandomPosInChunk(int x, int y, int z, int mask, BlockPos.MutableBlockPos out);
}