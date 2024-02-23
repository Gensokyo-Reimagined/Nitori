package net.gensokyoreimagined.gensouhacks.core;

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
