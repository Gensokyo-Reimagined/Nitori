package net.gensokyoreimagined.nitori.mixin.entity.navigation;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.Mob;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(PathNavigation.class)
public class CancelNavigationMixin {
    @Shadow @Final protected Mob mob;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void cancelTick(CallbackInfo ci) {
        if (mob.isPassenger()) {
            ci.cancel();
        }
    }

    @Inject(method = "createPath(Ljava/util/Set;Lnet/minecraft/world/entity/Entity;IZIF)Lnet/minecraft/world/level/pathfinder/Path;", at = @At(value = "INVOKE", target = "Ljava/util/Set;isEmpty()Z", shift = At.Shift.AFTER), cancellable = true)
    private void cancelFindPath(Set<BlockPos> positions, Entity target, int range, boolean useHeadPos, int distance, float followRange, CallbackInfoReturnable<Path> cir) {
        if (mob.isPassenger()) {
            cir.setReturnValue(null);
        }
    }
}