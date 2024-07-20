package net.gensokyoreimagined.nitori.mixin.math.random;

import net.gensokyoreimagined.nitori.common.math.random.RandomGeneratorRandom;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.RandomSupport;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RandomSource.class)
public interface RandomMixin {
    @Inject(method = "create(J)Lnet/minecraft/util/RandomSource;", at = @At(value = "HEAD"), cancellable = true)
    private static void fasterrandom$createInject(long seed, @NotNull CallbackInfoReturnable<RandomSource> cir) {
        cir.setReturnValue(new RandomGeneratorRandom(seed));
    }

    @Inject(method = "createNewThreadLocalInstance", at = @At(value = "HEAD"), cancellable = true)
    private static void fasterrandom$createLocalInject(@NotNull CallbackInfoReturnable<RandomSource> cir) {
        cir.setReturnValue(new RandomGeneratorRandom(ThreadLocalRandom.current().nextLong()));
    }

    @Inject(method = "createThreadSafe", at = @At(value = "HEAD"), cancellable = true)
    private static void fasterrandom$createThreadSafeInject(@NotNull CallbackInfoReturnable<RandomSource> cir) {
        cir.setReturnValue(new RandomGeneratorRandom(RandomSupport.generateUniqueSeed()));
    }
}