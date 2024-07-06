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

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.CompletableFuture;

@Mixin(LevelStorageSource.LevelStorageAccess.class)
public abstract class MixinLevelStorageAccess {
    @Shadow protected abstract void saveLevelData(CompoundTag nbt);

    @Redirect(method = "modifyLevelDataWithoutDatafix", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;saveLevelData(Lnet/minecraft/nbt/CompoundTag;)V"))
    private void gensouHacks$saveLevelData(LevelStorageSource.LevelStorageAccess levelStorageAccess, CompoundTag compoundTag) {
        Runnable writeRunnable = () -> saveLevelData(compoundTag);

        var ioExecutor = Util.backgroundExecutor();
        CompletableFuture.runAsync(writeRunnable, ioExecutor);
    }

    @Redirect(method = "saveDataTag(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/level/storage/WorldData;Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;saveLevelData(Lnet/minecraft/nbt/CompoundTag;)V"))
    private void gensouHacks$saveLevelData2(LevelStorageSource.LevelStorageAccess levelStorageAccess, CompoundTag compoundTag) {
        Runnable writeRunnable = () -> saveLevelData(compoundTag);

        var ioExecutor = Util.backgroundExecutor();
        CompletableFuture.runAsync(writeRunnable, ioExecutor);
    }
}
