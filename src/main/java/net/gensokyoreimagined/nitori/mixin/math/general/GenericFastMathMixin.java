package net.gensokyoreimagined.nitori.mixin.math.general;

import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.stream.IntStream;

@Mixin(Mth.class)
public class GenericFastMathMixin {

    /**
     * @author QPCrummer
     * @reason Slightly more optimized
     */
    @Overwrite
    public static boolean rayIntersectsAABB(Vec3 origin, Vec3 direction, AABB box) {

        double d = (box.minX + box.maxX) * 0.5;
        double e = (box.maxX - box.minX) * 0.5;
        double f = origin.x - d;
        if (Math.abs(f) > e && f * direction.x > 0.0) {
            return false;
        }

        double g = (box.minY + box.maxY) * 0.5;
        double h = (box.maxY - box.minY) * 0.5;
        double i = origin.y - g;
        if (Math.abs(i) > h && i * direction.y >= 0.0) {
            return false;
        }

        double j = (box.minZ + box.maxZ) * 0.5;
        double k = (box.maxZ - box.minZ) * 0.5;
        double l = origin.z - j;
        if (Math.abs(l) > k && l * direction.z >= 0.0) {
            return false;
        }

        double m = Math.abs(direction.x);
        double n = Math.abs(direction.y);
        double o = Math.abs(direction.z);
        double p = direction.y * l - direction.z * i;
        if (Math.abs(p) > h * o + k * n || Math.abs(direction.z * f - direction.x * l) > e * o + k * m) {
            return false;
        }

        return Math.abs(direction.x * i - direction.y * f) < e * n + h * m;
    }

    /**
     * @author QPCrummer
     * @reason Slightly more optimized
     */
    @Overwrite
    public static IntStream outFromOrigin(int seed, int lowerBound, int upperBound, int steps) {

        if (steps < 1 || seed < lowerBound || seed > upperBound) {
            return IntStream.empty();
        }

        return IntStream.iterate(seed, i -> {
            int nextValue = i + (i <= seed ? steps : -steps);
            return nextValue >= lowerBound && nextValue <= upperBound ? nextValue : i;
        });
    }
}