package net.gensokyoreimagined.nitori.mixin.entity.speed;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Credit Mirai patch #0046
@Mixin(Entity.class)
public abstract class EntitySpeedMixin {
    @Shadow public abstract Vec3 getDeltaMovement();

    @Shadow protected abstract float getBlockSpeedFactor();

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getBlockSpeedFactor()F"))
    private float redirectMultiplier(Entity instance) {
        if (this.getDeltaMovement().x == 0 && this.getDeltaMovement().z == 0) {
            return 1;
        } else {
            return this.getBlockSpeedFactor();
        }
    }
}