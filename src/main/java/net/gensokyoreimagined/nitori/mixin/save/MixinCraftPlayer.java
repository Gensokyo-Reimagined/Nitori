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
package net.gensokyoreimagined.nitori.mixin.save;

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
    private void nitori$savePlayerData(PlayerDataStorage instance, Player path) {
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
