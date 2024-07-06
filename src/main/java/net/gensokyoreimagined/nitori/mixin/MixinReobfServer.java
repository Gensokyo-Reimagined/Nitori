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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import space.vectrix.ignite.Blackboard;

import java.net.URISyntaxException;
import java.nio.file.Path;

@Mixin(targets = "io.papermc.paper.pluginremap.ReobfServer")
public class MixinReobfServer {
    @Inject(method = "serverJar", at = @At("HEAD"), cancellable = true)
    private static void serverJar(CallbackInfoReturnable<Path> cir) {
        try {
            var igniteGameJarPath = Blackboard.get(Blackboard.GAME_JAR);
            System.out.println("Nitori: ReobfServer.serverJar() called" + igniteGameJarPath);

            if (igniteGameJarPath.isPresent()) {
                System.out.println("Nitori: ReobfServer.serverJar() found Ignite, returning " + igniteGameJarPath.get());
                cir.setReturnValue(igniteGameJarPath.get());
                cir.cancel();
                return;
            }

            // This is a fallback in case the game jar path is not set for some reason
            var path = Path.of(MixinReobfServer.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            System.out.println("Nitori: ReobfServer.serverJar() tried loading, returning " + path);
            cir.setReturnValue(path);
            cir.cancel();
        } catch (final URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }
}
