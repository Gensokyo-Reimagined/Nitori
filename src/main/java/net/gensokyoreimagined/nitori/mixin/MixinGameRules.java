// Nitori Copyright (C) 2024 Gensokyo Reimagined
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
package net.gensokyoreimagined.nitori.mixin;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Map;

@Mixin(GameRules.class)
public class MixinGameRules {
    @Mutable
    @Shadow
    @Final
    private Map<GameRules.Key<?>, GameRules.Value<?>> rules;

    @Mutable
    @Shadow
    @Final
    private GameRules.Value<?>[] gameruleArray;

    @Accessor("GAME_RULE_TYPES")
    private static Map<GameRules.Key<?>, GameRules.Type<?>> getGameRuleTypes() {
        throw new AssertionError("Mixin failed to apply!");
    }

    @Inject(method = "<init>(Ljava/util/Map;)V", at = @At("TAIL"))
    public void privateInit(Map<GameRules.Key<?>, GameRules.Value<?>> rules, CallbackInfo info) {
        this.rules = new Object2ObjectOpenHashMap<>(rules);
    }

    @Inject(method = "<init>()V", at = @At("TAIL"))
    public void publicInit(CallbackInfo info) {
        // We're completely replacing this function... well, whatever. We abuse the singleton pattern.
        // Basically, we completely redo what the `this` constructor does.

        var rules = new Object2ObjectOpenHashMap<>((Map) getGameRuleTypes().entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, (entry) -> entry.getValue().createRule())));

        this.rules = rules;
        int arraySize = rules.keySet().stream().mapToInt((key) -> {
            return ((GameRules.Key<?>) key).gameRuleIndex;
        }).max().orElse(-1) + 1;
        GameRules.Value<?>[] values = new GameRules.Value[arraySize];

        Map.Entry entry;
        for(Iterator var4 = rules.entrySet().iterator(); var4.hasNext(); values[((GameRules.Key<?>)entry.getKey()).gameRuleIndex] = (GameRules.Value)entry.getValue()) {
            entry = (Map.Entry)var4.next();
        }

        this.gameruleArray = values;

        // Yeah. Don't do this at home, kids.
    }
}
