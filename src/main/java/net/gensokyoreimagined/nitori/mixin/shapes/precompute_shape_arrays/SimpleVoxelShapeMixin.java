package net.gensokyoreimagined.nitori.mixin.shapes.precompute_shape_arrays;

//import it.unimi.dsi.fastutil.doubles.DoubleList;
//import net.minecraft.core.Direction;
//import net.minecraft.world.phys.shapes.CubePointRange;
//import net.minecraft.world.phys.shapes.CubeVoxelShape;
//import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(CubeVoxelShape.class)
//public class SimpleVoxelShapeMixin {
//    private static final Direction.Axis[] AXIS = Direction.Axis.values();
//
//    private DoubleList[] parts;
//
//    @Inject(method = "<init>", at = @At("RETURN"))
//    private void CubeVoxelShape(DiscreteVoxelShape voxels, CallbackInfo ci) {
//        this.parts = new DoubleList[AXIS.length];
//
//        for (Direction.Axis axis : AXIS) {
//            this.parts[axis.ordinal()] = new CubePointRange(voxels.getSize(axis));
//        }
//    }
//
//    /**
//     * @author JellySquid
//     * @reason Use the cached array
//     */
//    @Overwrite
//    public DoubleList getCoords(Direction.Axis axis) {
//        return this.parts[axis.ordinal()];
//    }
//
//}