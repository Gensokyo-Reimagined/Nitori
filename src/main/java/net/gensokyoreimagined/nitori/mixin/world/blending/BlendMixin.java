package net.gensokyoreimagined.nitori.mixin.world.blending;

import net.gensokyoreimagined.nitori.common.math.FasterMathUtil;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Credit Gale patch #0019
@Mixin(Blender.class)
public class BlendMixin {
    @Redirect(method = "heightToOffset", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;positiveModulo(DD)D"))
    private static double redirectBlendOffset(double dividend, double divisor) {
        return FasterMathUtil.positiveModuloForPositiveIntegerDivisor(dividend, divisor);
    }
}
