package net.gensokyoreimagined.nitori.mixin.unapplied.inline_height;

//import net.minecraft.core.RegistryAccess;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.core.Holder;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.level.LevelHeightAccessor;
//import net.minecraft.world.level.storage.WritableLevelData;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.dimension.DimensionType;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import java.util.function.Supplier;
//
///**
// * Implement world height related methods directly instead of going through WorldView and Dimension
// */
//@Mixin(Level.class)
//public abstract class WorldMixin implements LevelHeightAccessor {
//    @Unique
//    private int nitori$getMinBuildHeight;
//    @Unique
//    private int nitori$getHeight;
//    @Unique
//    private int nitori$getSectionsCount;
//
//    @Inject(
//            method = "<init>",
//            at = @At("RETURN")
//    )
//    private void initHeightCache(WritableLevelData worldData, ResourceKey<?> registryKey, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<?> profiler, boolean isClientSide, boolean debug, long biomeManager, int maxChainedNeighborUpdates, CallbackInfo ci)
//    {
//        this.nitori$getHeight = dimensionTypeRegistration.value().height();
//        this.nitori$getMinBuildHeight = dimensionTypeRegistration.value().minY();
//        this.nitori$getSectionsCount = this.nitori$getMinBuildHeight + this.nitori$getHeight - 1;
//    }
//
//    @Override
//    public int getHeight() {
//        return this.nitori$getHeight;
//    }
//
//    @Override
//    public int getMinBuildHeight() {
//        return this.nitori$getMinBuildHeight;
//    }
//
//    @Override
//    public int getSectionsCount() {
//        return ((this.nitori$getSectionsCount >> 4) + 1) - (this.nitori$getMinBuildHeight >> 4);
//    }
//
//    @Override
//    public int getMinSection() {
//        return this.nitori$getMinBuildHeight >> 4;
//    }
//
//    @Override
//    public int getMaxSection() {
//        return (this.nitori$getSectionsCount >> 4) + 1;
//    }
//
//    @Override
//    public boolean isOutsideBuildHeight(BlockPos pos) {
//        int y = pos.getY();
//        return (y < this.nitori$getMinBuildHeight) || (y > this.nitori$getSectionsCount);
//    }
//
//    @Override
//    public boolean isOutsideBuildHeight(int y) {
//        return (y < this.nitori$getMinBuildHeight) || (y > this.nitori$getSectionsCount);
//    }
//
//    @Override
//    public int getSectionIndex(int y) {
//        return (y >> 4) - (this.nitori$getMinBuildHeight >> 4);
//    }
//
//    @Override
//    public int getSectionIndexFromSectionY(int coord) {
//        return coord - (this.nitori$getMinBuildHeight >> 4);
//
//    }
//
//    @Override
//    public int getSectionYFromSectionIndex(int index) {
//        return index + (this.nitori$getMinBuildHeight >> 4);
//    }
//
//    @Override
//    public int getMaxBuildHeight() {
//        return this.nitori$getSectionsCount + 1;
//    }
//}

//im stupid, couldn't figure out why initHeightCache is dying