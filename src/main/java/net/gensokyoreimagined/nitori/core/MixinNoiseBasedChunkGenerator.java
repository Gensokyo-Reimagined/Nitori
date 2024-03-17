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
package net.gensokyoreimagined.nitori.core;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseBasedChunkGenerator.class)
public class MixinNoiseBasedChunkGenerator {
    @Unique
    private Supplier<Integer> gensouHacks$cachedSeaLevel;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings, CallbackInfo ci) {
        // Cache the sea level - cannot be done in the constructor as the settings are not yet available
        this.gensouHacks$cachedSeaLevel = Suppliers.memoize(() -> settings.value().seaLevel());
    }

    @Inject(method = "getSeaLevel", at = @At("HEAD"), cancellable = true)
    public void getSeaLevel(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(this.gensouHacks$cachedSeaLevel.get());
        cir.cancel();
    }
}
