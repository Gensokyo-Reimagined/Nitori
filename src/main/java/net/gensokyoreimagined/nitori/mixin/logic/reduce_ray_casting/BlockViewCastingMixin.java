package net.gensokyoreimagined.nitori.mixin.logic.reduce_ray_casting;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.ClipContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Credit to PaperMC Patch #0687 and #684
 */
@Mixin(BlockGetter.class)
public interface BlockViewCastingMixin extends LevelHeightAccessor {
    @Shadow BlockState getBlockState(BlockPos pos);

    @Shadow @Nullable BlockHitResult clipWithInteractionOverride(Vec3 start, Vec3 end, BlockPos pos, VoxelShape shape, BlockState state);

    @Shadow static <T, C> T traverseBlocks(Vec3 start, Vec3 end, C context, BiFunction<C, BlockPos, T> blockHitFactory, Function<C, T> missFactory){return null;}

    /**
     * @author QPCrummer
     * @reason Optimize
     */
    @Overwrite
    default BlockHitResult clip(ClipContext context) {
        return traverseBlocks(context.getFrom(), context.getTo(), context, (innerContext, pos) -> {
            BlockState blockState = this.getBlockState(pos);
            if (blockState.isAir()) return null;
            FluidState fluidState = blockState.getFluidState();
            Vec3 Vec3 = innerContext.getFrom();
            Vec3 vec3d2 = innerContext.getTo();
            VoxelShape voxelShape = innerContext.getBlockShape(blockState, (BlockGetter)(Object)this, pos);
            BlockHitResult blockHitResult = this.clipWithInteractionOverride(Vec3, vec3d2, pos, voxelShape, blockState);
            VoxelShape voxelShape2 = innerContext.getFluidShape(fluidState, (BlockGetter)(Object)this, pos);
            BlockHitResult blockHitResult2 = voxelShape2.clip(Vec3, vec3d2, pos);
            double d = blockHitResult == null ? Double.MAX_VALUE : innerContext.getFrom().distanceToSqr(blockHitResult.getLocation());
            double e = blockHitResult2 == null ? Double.MAX_VALUE : innerContext.getTo().distanceToSqr(blockHitResult2.getLocation());
            return d <= e ? blockHitResult : blockHitResult2;
        }, (innerContext) -> {
            Vec3 Vec3 = innerContext.getFrom().subtract(innerContext.getTo());
            return BlockHitResult.miss(innerContext.getTo(), Direction.getNearest(Vec3.x, Vec3.y, Vec3.z), BlockPos.containing(innerContext.getTo()));
        });
    }
}