package net.gensokyoreimagined.nitori.mixin.collections.mob_spawning;

import com.google.common.collect.Maps;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(MobSpawnSettings.class)
public class SpawnSettingsMixin {
    @Mutable
    @Shadow
    @Final
    private Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> spawners;


    @Inject(method = "<init>", at = @At("RETURN"))
    private void reinit(float creatureSpawnProbability, Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> spawners, Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> spawnCosts, CallbackInfo ci) {
        Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> spawns = Maps.newEnumMap(MobCategory.class);

        for (Map.Entry<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> entry : this.spawners.entrySet()) {
            spawns.put(entry.getKey(), entry.getValue());
        }

        this.spawners = spawns;
    }
}