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

@Mixin(targets = "gg.pufferfish.pufferfish.simd.SIMDDetection")
public abstract class MixinSpongeSIMD {
  @Inject(method = "getJavaVersion", at = @At("RETURN"), cancellable = true)
  private static void loadPufferfishConfig(CallbackInfoReturnable<Integer> callback) {
    // We troll the Pufferfish developers by changing the return value of the method
    // System.out.println("Thought that it was " + callback.getReturnValue() + " for java version");
    if (callback.getReturnValue() == 21) {
      System.out.println("Successfully trolled Pufferfish into thinking we're on Java 19!");
      callback.setReturnValue(19);
    }
  }
}
