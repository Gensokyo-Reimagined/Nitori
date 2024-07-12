package net.gensokyoreimagined.nitori.mixin.entity.sprinting_particles;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract Level level();

    @Redirect(
            method = "baseTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;canSpawnSprintParticle()Z")
    )
    private boolean skipParticlesOnServerSide(Entity instance) {
        if (instance.level().isClientSide()) {
            return instance.canSpawnSprintParticle();
        }
        return false;
    }
}