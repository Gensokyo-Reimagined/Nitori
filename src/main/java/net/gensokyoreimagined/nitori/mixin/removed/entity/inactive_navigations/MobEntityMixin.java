package net.gensokyoreimagined.nitori.mixin.removed.entity.inactive_navigations;

//import net.gensokyoreimagined.nitori.common.entity.NavigatingEntity;
//import net.gensokyoreimagined.nitori.common.world.ServerWorldExtended;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.ai.navigation.PathNavigation;
//import net.minecraft.world.entity.Mob;
//import net.minecraft.world.level.Level;
//import org.spongepowered.asm.mixin.Intrinsic;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//@Mixin(Mob.class)
//public abstract class MobEntityMixin extends Entity implements NavigatingEntity {
//    private PathNavigation nitori$registeredNavigation;
//
//    public MobEntityMixin(EntityType<?> type, Level world) {
//        super(type, world);
//    }
//
//    @Shadow
//    public abstract PathNavigation getNavigation();
//
//    @Override
//    public boolean lithium$isRegisteredToWorld() {
//        return this.nitori$registeredNavigation != null;
//    }
//
//    @Override
//    public void lithium$setRegisteredToWorld(PathNavigation navigation) {
//        this.nitori$registeredNavigation = navigation;
//    }
//
//    @Override
//    public PathNavigation lithium$getRegisteredNavigation() {
//        return this.nitori$registeredNavigation;
//    }
//
//    @Inject(method = "startRiding", at = @At("RETURN"))
//    private void onNavigationReplacement(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir) {
//        this.lithium$updateNavigationRegistration();
//    }
//
//    @Override
//    @Intrinsic
//    public void stopRiding() {
//        super.stopRiding();
//    }
//
//    @SuppressWarnings({"MixinAnnotationTarget", "UnresolvedMixinReference"})
//    @Inject(method = "stopRiding()V", at = @At("RETURN"))
//    private void updateOnStopRiding(CallbackInfo ci) {
//        this.lithium$updateNavigationRegistration();
//    }
//
//    @Override
//    public void lithium$updateNavigationRegistration() {
//        if (this.lithium$isRegisteredToWorld()) {
//            PathNavigation navigation = this.getNavigation();
//            if (this.nitori$registeredNavigation != navigation) {
//                ((ServerWorldExtended) this.level()).lithium$setNavigationInactive((Mob) (Object) this);
//                this.nitori$registeredNavigation = navigation;
//
//                if (navigation.getPath() != null) {
//                    ((ServerWorldExtended) this.level()).lithium$setNavigationActive((Mob) (Object) this);
//                }
//            }
//        }
//    }
//
//}