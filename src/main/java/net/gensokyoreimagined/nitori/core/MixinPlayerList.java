package net.gensokyoreimagined.nitori.core;

import net.minecraft.Util;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.PlayerDataStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.CompletableFuture;

@Mixin(PlayerList.class)
public class MixinPlayerList {
    @Final
    @Shadow
    public PlayerDataStorage playerIo;

    @Redirect(method = "save", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/PlayerDataStorage;save(Lnet/minecraft/world/entity/player/Player;)V"))
    private void gensouHacks$savePlayerData(PlayerDataStorage instance, Player player) {
        Runnable writeRunnable = () -> playerIo.save(player);

        var ioExecutor = Util.backgroundExecutor();
        CompletableFuture.runAsync(writeRunnable, ioExecutor);
    }
}
