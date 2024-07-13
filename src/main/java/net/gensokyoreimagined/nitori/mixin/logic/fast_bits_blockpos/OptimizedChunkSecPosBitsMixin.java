package net.gensokyoreimagined.nitori.mixin.logic.fast_bits_blockpos;

//TODO: impl this

//import net.minecraft.core.BlockPos;
//import net.minecraft.world.level.ChunkPos;
//import net.minecraft.core.SectionPos;
//import net.minecraft.core.Vec3i;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
//import org.spongepowered.asm.mixin.Shadow;
//
//import java.util.stream.Stream;
//
//@Mixin(SectionPos.class)
//public abstract class OptimizedChunkSecPosBitsMixin extends Vec3i {
//    @Shadow
//    public static Stream<SectionPos> betweenClosedStream(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
//        return null;
//    }
//
//
//
//    public OptimizedChunkSecPosBitsMixin(int x, int y, int z) {
//        super(x, y, z);
//    }
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public static SectionPos of(BlockPos pos) {
//        return ChunkSectionPosAccessor.invokeChunkSectionPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4);
//    }
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public static SectionPos of(long packed) {
//        return ChunkSectionPosAccessor.invokeChunkSectionPos((int) (packed >> 42), (int) (packed << 44 >> 44), (int) (packed << 22 >> 42));
//    }
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public static long offset(long packed, int x, int y, int z) {
//        return (((long) ((int) (packed >> 42) + x) & 4194303L) << 42) | (((long) ((int) (packed << 44 >> 44) + y) & 1048575L)) | (((long) ((int) (packed << 22 >> 42) + z) & 4194303L) << 20);
//    }
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public static short sectionRelativePos(BlockPos pos) {
//        return (short) ((pos.getX() & 15) << 8 | (pos.getZ() & 15) << 4 | pos.getY() & 15);
//    }
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public final int minBlockX() {
//        return this.getX() << 4;
//    }
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public int getMinY() {
//        return this.getY() << 4;
//    }
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public int getMinZ() {
//        return this.getZ() << 4;
//    }
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public static long blockToSection(long blockPos) {
//        return (((long) (int) (blockPos >> 42) & 4194303L) << 42) | (((long) (int) ((blockPos << 52) >> 56) & 1048575L)) | (((long) (int) ((blockPos << 26) >> 42) & 4194303L) << 20);
//    }
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public static long asLong(int x, int y, int z) {
//        return (((long) x & 4194303L) << 42) | (((long) y & 1048575L)) | (((long) z & 4194303L) << 20);
//    }
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public long asLong() {
//        return (((long) getX() & 4194303L) << 42) | (((long) getY() & 1048575L)) | (((long) getZ() & 4194303L) << 20);
//    }
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public static Stream<SectionPos> betweenClosedStream(SectionPos center, int radius) {
//        return betweenClosedStream(center.getX() - radius, center.getY() - radius, center.getZ() - radius, center.getX() + radius, center.getY() + radius, center.getZ() + radius);
//    }
//
//    /**
//     * @author QPCrummer
//     * @reason Inline
//     */
//    @Overwrite
//    public static Stream<SectionPos> betweenClosedStream(ChunkPos center, int radius, int minY, int maxY) {
//        return betweenClosedStream(center.x - radius, 0, center.z - radius, center.x + radius, 15, center.z + radius);
//    }
//
//}