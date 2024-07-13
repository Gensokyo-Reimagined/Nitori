package net.gensokyoreimagined.nitori.mixin.math.vec;

import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.*;

@Mixin(Vec3.class)
public class FastMathVec3DMixin {
    @Mutable
    @Shadow
    @Final
    public double x;

    @Mutable
    @Shadow @Final public double y;

    @Mutable
    @Shadow @Final public double z;
    private Vec3 cachedNormalized;

    /**
     * @author QPCrummer
     * @reason Cache normalized Vec
     */
    @Overwrite
    public Vec3 normalize() {
        if (cachedNormalized == null) {
            double squaredLength = x * x + y * y + z * z;
            if (squaredLength < 1.0E-8) {
                cachedNormalized = Vec3.ZERO;
            } else {
                double invLength = 1.0 / Math.sqrt(squaredLength);
                cachedNormalized = new Vec3(x * invLength, y * invLength, z * invLength);
            }
        }
        return cachedNormalized;
    }
}