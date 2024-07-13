package net.gensokyoreimagined.nitori.mixin.world.portal_checks;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PortalShape.class})
public class DisablePortalChecksMixin {
    @Inject(
            at = {@At("HEAD")},
            method = {"findCollisionFreePosition"},
            cancellable = true
    )
    private static void init(Vec3 fallback, ServerLevel world, Entity entity, EntityDimensions dimensions, CallbackInfoReturnable<Vec3> cir) {
        cir.setReturnValue(fallback);
    }
}
