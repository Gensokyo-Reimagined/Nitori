package net.gensokyoreimagined.nitori.mixin.alloc;

import net.gensokyoreimagined.nitori.common.world.ChunkRandomSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Level.class)
public class WorldMixin implements ChunkRandomSource {
    protected int lcgBlockSeed;


    @Override
    public void lithium$getRandomPosInChunk(int x, int y, int z, int mask, BlockPos.MutableBlockPos out) {
        this.lcgBlockSeed = this.lcgBlockSeed * 3 + 1013904223;
        int rand = this.lcgBlockSeed >> 2;
        out.set(x + (rand & 15), y + (rand >> 16 & mask), z + (rand >> 8 & 15));
    }
}