package net.gensokyoreimagined.nitori.mixin.removed.alloc.chunk_random;

//import net.gensokyoreimagined.nitori.common.world.ChunkRandomSource;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.level.Level;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//
//@Mixin(Level.class)
//public class WorldMixin implements ChunkRandomSource {
//    @Shadow
//    protected int randValue;
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void nitori$getRandomPosInChunk(int x, int y, int z, int mask, BlockPos.MutableBlockPos out) {
//        this.randValue = this.randValue * 3 + 1013904223;
//        int rand = this.randValue >> 2;
//        out.set(x + (rand & 15), y + (rand >> 16 & mask), z + (rand >> 8 & 15));
//    }
//}