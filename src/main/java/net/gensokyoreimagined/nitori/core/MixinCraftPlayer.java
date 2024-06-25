package net.gensokyoreimagined.nitori.core;

import net.minecraft.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.PlayerDataStorage;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.CompletableFuture;

@Mixin(CraftPlayer.class)
public class MixinCraftPlayer {
    @Redirect(method = "saveData", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/PlayerDataStorage;save(Lnet/minecraft/world/entity/player/Player;)V"))
    private void gensouHacks$savePlayerData(PlayerDataStorage instance, Player path) {
        Runnable writeRunnable = () -> {
            var craftPlayer = (CraftPlayer) (Object) this;
            var server = craftPlayer.getServer();

            if (server instanceof CraftServer craftServer) {
                var handle = craftPlayer.getHandle();
                var playerIo = craftServer.getHandle().playerIo;
                playerIo.save(handle);
            }
        };

        var ioExecutor = Util.backgroundExecutor();
        CompletableFuture.runAsync(writeRunnable, ioExecutor);
    }
}
