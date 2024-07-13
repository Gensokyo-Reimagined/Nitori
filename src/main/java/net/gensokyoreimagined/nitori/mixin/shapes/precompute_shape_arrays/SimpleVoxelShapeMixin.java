package net.gensokyoreimagined.nitori.mixin.shapes.precompute_shape_arrays;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CubePointRange;
import net.minecraft.world.phys.shapes.CubeVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Constructor;

@Mixin(CubeVoxelShape.class)
public class SimpleVoxelShapeMixin {
    private static final Direction.Axis[] AXIS = Direction.Axis.values();

    private DoubleList[] list;

    @Unique
    private static Constructor<CubePointRange> nitori$cubePointRangeConstructor;

    @Unique
    private CubePointRange nitori$cubePointRange(int sectionCount) {
        try {
            if (nitori$cubePointRangeConstructor == null) {
                nitori$cubePointRangeConstructor = CubePointRange.class.getDeclaredConstructor(int.class);
                nitori$cubePointRangeConstructor.setAccessible(true);
            }

            return nitori$cubePointRangeConstructor.newInstance(sectionCount);
        } catch (Exception ex) {
            throw new AssertionError("Failed to find CubePointRange constructor - " + ex.getMessage(), ex);
        }
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstructed(DiscreteVoxelShape voxels, CallbackInfo ci) {
        this.list = new DoubleList[AXIS.length];

        for (Direction.Axis axis : AXIS) {
            this.list[axis.ordinal()] = nitori$cubePointRange(voxels.getSize(axis));
        }
    }

    /**
     * @author JellySquid
     * @reason Use the cached array
     */
    @Overwrite
    public DoubleList getCoords(Direction.Axis axis) {
        return this.list[axis.ordinal()];
    }

}