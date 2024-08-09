package net.gensokyoreimagined.nitori.mixin.unapplied.experimental.entity.block_caching.block_support;

//import net.gensokyoreimagined.nitori.common.entity.block_tracking.BlockCache;
//import net.gensokyoreimagined.nitori.common.entity.block_tracking.BlockCacheProvider;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.phys.Vec3;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(Entity.class)
//public abstract class EntityMixin implements BlockCacheProvider {
//    @Inject(
//            method = "checkSupportingBlock", cancellable = true,
//            at = @At(
//                    value = "INVOKE", shift = At.Shift.BEFORE,
//                    target = "Lnet/minecraft/world/entity/Entity;getBoundingBox()Lnet/minecraft/world/phys/AABB;"
//            )
//    )
//    private void cancelIfSkippable(boolean onGround, Vec3 movement, CallbackInfo ci) {
//        if (movement == null || (movement.x == 0 && movement.z == 0)) {
//            //noinspection ConstantConditions
//            BlockCache bc = this.getUpdatedBlockCache((Entity) (Object) this);
//            if (bc.canSkipSupportingBlockSearch()) {
//                ci.cancel();
//            }
//        }
//    }
//
//    @Inject(
//            method = "checkSupportingBlock",
//            at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/world/entity/Entity;getBoundingBox()Lnet/minecraft/world/phys/AABB;")
//    )
//    private void cacheSupportingBlockSearch(CallbackInfo ci) {
//        BlockCache bc = this.lithium$getBlockCache();
//        if (bc.isTracking()) {
//            bc.setCanSkipSupportingBlockSearch(true);
//        }
//    }
//
//    @Inject(
//            method = "checkSupportingBlock",
//            at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/world/World;findSupportingBlockPos(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/Optional;")
//    )
//    private void uncacheSupportingBlockSearch(CallbackInfo ci) {
//        BlockCache bc = this.lithium$getBlockCache();
//        if (bc.isTracking()) {
//            bc.setCanSkipSupportingBlockSearch(false);
//        }
//    }
//
//    @Inject(
//            method = "checkSupportingBlock",
//            at = @At(value = "INVOKE", target = "Ljava/util/Optional;empty()Ljava/util/Optional;", remap = false)
//    )
//    private void uncacheSupportingBlockSearch1(boolean onGround, Vec3 movement, CallbackInfo ci) {
//        BlockCache bc = this.lithium$getBlockCache();
//        if (bc.isTracking()) {
//            bc.setCanSkipSupportingBlockSearch(false);
//        }
//    }
//}