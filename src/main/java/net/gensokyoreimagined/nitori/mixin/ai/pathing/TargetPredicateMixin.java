package net.gensokyoreimagined.nitori.mixin.ai.pathing;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Credit: Leaves patch #0026
 */
@Mixin(TargetingConditions.class)
public class TargetPredicateMixin {
    @Shadow private double range;

    @Inject(method = "test", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getVisibilityPercent(Lnet/minecraft/world/entity/Entity;)D", shift = At.Shift.BEFORE), cancellable = true)
    private void quickCancelPathFinding(LivingEntity baseEntity, LivingEntity targetEntity, CallbackInfoReturnable<Boolean> cir, @Share("dist")LocalDoubleRef doubleRef) {
        double f = baseEntity.distanceToSqr(targetEntity.getX(), targetEntity.getY(), targetEntity.getZ());
        doubleRef.set(f);
        if (f > this.range * this.range) {
            cir.setReturnValue(false);
        }
    }

    @Redirect(method = "test", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;distanceToSqr(DDD)D"))
    private double borrowValueFromOtherMixin(LivingEntity instance, double x, double y, double z, @Share("dist")LocalDoubleRef doubleRef) {
        return doubleRef.get();
    }
}