package net.gensokyoreimagined.nitori.mixin.logic.fast_rotations;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.Rotations;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Credit to PaperMC patch #0565
 */
//TODO Rewrite this so that pitch, yaw, and roll can be inlined properly
@Mixin(Rotations.class)
public class EulerAngleMixin {

    @Mutable
    @Unique
    @Final
    private static float x;
    @Unique
    @Final
    @Mutable
    private static float y;
    @Unique
    @Final
    @Mutable
    private static float z;

    @Redirect(method = "<init>(FFF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/core/Rotations;x:F"))
    private void injectedPitch(Rotations instance, float value, @Local(ordinal = 0, argsOnly = true) float pitch) {
        EulerAngleMixin.x = pitch;
    }

    @Redirect(method = "<init>(FFF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/core/Rotations;y:F"))
    private void injectedYaw(Rotations instance, float value, @Local(ordinal = 0, argsOnly = true) float yaw) {
        EulerAngleMixin.y = yaw;
    }

    @Redirect(method = "<init>(FFF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/core/Rotations;z:F"))
    private void injectedRoll(Rotations instance, float value, @Local(ordinal = 0, argsOnly = true) float roll) {
        EulerAngleMixin.z = roll;
    }

    /**
     * @author QPCrummer
     * @reason make fast
     */
    @Overwrite
    public float getX() {
        return x;
    }

    /**
     * @author QPCrummer
     * @reason make fast
     */
    @Overwrite
    public float getY() {
        return y;
    }

    /**
     * @author QPCrummer
     * @reason make fast
     */
    @Overwrite
    public float getZ() {
        return z;
    }

    /**
     * @author QPCrummer
     * @reason make fast
     */
    @Overwrite
    public float getWrappedX() {
        return Mth.wrapDegrees(x);
    }

    /**
     * @author QPCrummer
     * @reason make fast
     */
    @Overwrite
    public float getWrappedY() {
        return Mth.wrapDegrees(y);
    }

    /**
     * @author QPCrummer
     * @reason make fast
     */
    @Overwrite
    public float getWrappedZ() {
        return Mth.wrapDegrees(z);
    }

    /**
     * @author QPCrummer
     * @reason make fast
     */
    @Overwrite
    public boolean equals(Object o) {
        if (!(o instanceof Rotations eulerAngle)) {
            return false;
        } else {
            return x == eulerAngle.getX() && y == eulerAngle.getY() && z == eulerAngle.getZ();
        }
    }

    /**
     * @author QPCrummer
     * @reason make fast
     */
    @Overwrite
    public ListTag save() {
        ListTag ListTag = new ListTag();
        ListTag.add(FloatTag.valueOf(x));
        ListTag.add(FloatTag.valueOf(y));
        ListTag.add(FloatTag.valueOf(z));
        return ListTag;
    }
}