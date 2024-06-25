package net.gensokyoreimagined.nitori.core;

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
