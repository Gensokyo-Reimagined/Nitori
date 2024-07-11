package net.gensokyoreimagined.nitori.mixin.math.rounding;

import net.gensokyoreimagined.nitori.common.math.FasterMathUtil;
import net.minecraft.world.phys.shapes.Shapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Shapes.class, priority = 1010)
public class FastRoundingVoxShapeMixin {
    @Redirect(
            method = "create(DDDDDD)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            require = 0,
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;round(D)J"))
    private static long fasterRoundCuboid(double value) {
        return FasterMathUtil.round(value);
    }

    @Redirect(
            method = "findBits",
            require = 0,
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;round(D)J"))
    private static long fasterRoundResolution(double value) {
        return FasterMathUtil.round(value);
    }
}