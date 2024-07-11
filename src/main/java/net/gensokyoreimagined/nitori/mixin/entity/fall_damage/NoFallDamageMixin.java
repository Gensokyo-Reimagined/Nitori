package net.gensokyoreimagined.nitori.mixin.entity.fall_damage;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class NoFallDamageMixin {

    @Shadow public abstract boolean onGround();

    @Shadow public abstract void resetFallDistance();


    @Inject(method = "checkFallDamage", at = @At("HEAD"), cancellable = true)
    private void cancelFallDamage(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition, CallbackInfo ci) {
        if (!(((Entity)(Object)this) instanceof LivingEntity)) {
            if (this.onGround()) {
                this.resetFallDistance();
            }
            ci.cancel();
        }
    }

}