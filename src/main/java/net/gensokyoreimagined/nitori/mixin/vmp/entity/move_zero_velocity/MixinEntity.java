package net.gensokyoreimagined.nitori.mixin.vmp.entity.move_zero_velocity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {

    @Shadow private AABB bb;
    @Unique
    private boolean boundingBoxChanged = false;

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void onMove(MoverType movementType, Vec3 movement, CallbackInfo ci) {
        if (!boundingBoxChanged && movement.equals(Vec3.ZERO)) {
            ci.cancel();
            boundingBoxChanged = false;
        }
    }

    @Inject(method = "setBoundingBox", at = @At("HEAD"))
    private void onBoundingBoxChanged(AABB boundingBox, CallbackInfo ci) {
        if (!this.bb.equals(boundingBox)) boundingBoxChanged = true;
    }

}