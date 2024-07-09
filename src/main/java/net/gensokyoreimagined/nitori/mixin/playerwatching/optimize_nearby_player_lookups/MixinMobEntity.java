package net.gensokyoreimagined.nitori.mixin.playerwatching.optimize_nearby_player_lookups;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;

@Mixin(Mob.class)
public abstract class MixinMobEntity extends LivingEntity {

    protected MixinMobEntity(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(method = "checkDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;findNearbyPlayer(Lnet/minecraft/world/entity/Entity;DLjava/util/function/Predicate;)Lnet/minecraft/world/entity/player/Player;"))
    private Player redirectGetClosestPlayer(Level instance, Entity entity, double v, Predicate predicate) {
        final Player closestPlayer = instance.getNearestPlayer(entity, this.getType().getCategory().getDespawnDistance());
        if (closestPlayer != null) {
            return closestPlayer;
        } else {
            final List<? extends Player> players = this.level().players();
            if (players.isEmpty()) return null;
            return players.get(0);
        }
    }

}