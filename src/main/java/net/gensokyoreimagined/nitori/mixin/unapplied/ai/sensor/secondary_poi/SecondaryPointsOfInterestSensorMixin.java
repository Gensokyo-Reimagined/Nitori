package net.gensokyoreimagined.nitori.mixin.unapplied.ai.sensor.secondary_poi;

import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SecondaryPoiSensor;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SecondaryPoiSensor.class)
public class SecondaryPointsOfInterestSensorMixin {

    @Inject(
            method = "doTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/Villager;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void skipUselessSense(ServerLevel serverWorld, Villager villagerEntity, CallbackInfo ci) {
        if (villagerEntity.getVillagerData().getProfession().secondaryPoi().isEmpty()) {
            villagerEntity.getBrain().eraseMemory(MemoryModuleType.SECONDARY_JOB_SITE);
            ci.cancel();
        }
    }
}