package net.gensokyoreimagined.nitori.mixin.removed.logic.fast_bits_blockpos;

//import net.minecraft.core.BlockPos;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
//
///**
// * Credit to PaperMC Patch #0421
// */
//@Mixin(BlockPos.class)
//public class OptimizedBlockPosBitsMixin {
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public static long offset(long value, int x, int y, int z) {
//        return asLong((int) (value >> 38) + x, (int) ((value << 52) >> 52) + y, (int) ((value << 26) >> 38) + z);
//    }
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public static int getX(long packedPos) {
//        return (int) (packedPos >> 38);
//    }
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public static int getY(long packedPos) {
//        return (int) ((packedPos << 52) >> 52);
//    }
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public static int getZ(long packedPos) {
//        return (int) ((packedPos << 26) >> 38);
//    }
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public static BlockPos of(long packedPos) {
//        return new BlockPos((int) (packedPos >> 38), (int) ((packedPos << 52) >> 52), (int) ((packedPos << 26) >> 38));
//    }
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public static long asLong(int x, int y, int z) {
//        return (((long) x & (long) 67108863) << 38) | (((long) y & (long) 4095)) | (((long) z & (long) 67108863) << 12);
//    }
//
//}