package net.gensokyoreimagined.nitori.mixin.math.rounding;

import net.gensokyoreimagined.nitori.common.math.FasterMathUtil;
import net.minecraft.world.level.levelgen.SurfaceSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SurfaceSystem.class)
public class FastRoundingSurfaceBuildMixin {
    @Redirect(
            method = "getBand",
            require = 0,
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;round(D)J"))
    private long fasterRound(double value) {
        return FasterMathUtil.round(value);
    }
}