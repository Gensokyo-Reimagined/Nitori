package net.gensokyoreimagined.nitori.mixin.math.fast_blockops;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The vector of each Direction is usually stored inside another object, which introduces indirection and makes things
 * harder for the JVM to optimize. This patch simply hoists the offset fields to the Direction enum itself.
 */
@Mixin(Direction.class)
public class DirectionMixin {
    @Unique
    private int nitori$getStepX, nitori$getStepY, nitori$getStepZ;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void reinit(String enumName, int ordinal, int id, int idOpposite, int idHorizontal, String name, Direction.AxisDirection direction, Direction.Axis axis, Vec3i vector, CallbackInfo ci) {
        this.nitori$getStepX = vector.getX();
        this.nitori$getStepY = vector.getY();
        this.nitori$getStepZ = vector.getZ();
    }

    /**
     * @reason Avoid indirection to aid inlining
     * @author JellySquid
     */
    @Overwrite
    public int getStepX() {
        return this.nitori$getStepX;
    }

    /**
     * @reason Avoid indirection to aid inlining
     * @author JellySquid
     */
    @Overwrite
    public int getStepY() {
        return this.nitori$getStepY;
    }

    /**
     * @reason Avoid indirection to aid inlining
     * @author JellySquid
     */
    @Overwrite
    public int getStepZ() {
        return this.nitori$getStepZ;
    }
}