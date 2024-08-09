package net.gensokyoreimagined.nitori.mixin.unapplied.ai.pathing;

import net.gensokyoreimagined.nitori.common.ai.pathing.BlockStatePathingCache;
import net.gensokyoreimagined.nitori.common.world.blockview.SingleBlockBlockView;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator; //LandPathNodeMaker
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.core.BlockPos;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class AbstractBlockStateMixin implements BlockStatePathingCache {
    private PathType pathNodeType = null;
    private PathType pathNodeTypeNeighbor = null;

    @Unique
    private static final Method nitori$getPathTypeFromStateMethod;

    static {
        try {
            nitori$getPathTypeFromStateMethod = WalkNodeEvaluator.class.getDeclaredMethod("getPathTypeFromState", BlockGetter.class, BlockPos.class);
        } catch (NoSuchMethodException e) {
            throw new AssertionError("Failed to reflect getCommonNodeType method", e);
        }
        nitori$getPathTypeFromStateMethod.setAccessible(true);
    }

    @Unique
    private static PathType nitori$getPathTypeFromState(BlockGetter getter, BlockPos position) throws SingleBlockBlockView.SingleBlockViewException, ClassCastException {
        try {
            return (PathType) nitori$getPathTypeFromStateMethod.invoke(null, getter, position);
        } catch (InvocationTargetException e) {
            var cause = e.getCause();

            if (cause instanceof SingleBlockBlockView.SingleBlockViewException) {
                throw (SingleBlockBlockView.SingleBlockViewException) cause;
            }

            if (cause instanceof ClassCastException) {
                throw (ClassCastException) cause;
            }

            throw new AssertionError("Get path type failed!", cause);
        } catch (IllegalAccessException e) {
            throw new AssertionError("Failed to reflect method", e);
        }
    }

    @Inject(method = "initCache", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        // Reset the cached path node types, to ensure they are re-calculated.
        this.pathNodeType = null;
        this.pathNodeTypeNeighbor = null;

        BlockState state = this.asState();

        SingleBlockBlockView singleBlockBlockView = SingleBlockBlockView.of(state, BlockPos.ZERO);
        try {
            this.pathNodeType = Validate.notNull(nitori$getPathTypeFromState(singleBlockBlockView, BlockPos.ZERO), "Block has no common path node type!");
        } catch (SingleBlockBlockView.SingleBlockViewException | ClassCastException e) {
            //This is usually hit by shulker boxes, as their hitbox depends on the block entity, and the node type depends on the hitbox
            //Also catch ClassCastException in case some modded code casts BlockView to ChunkCache
            this.pathNodeType = null;
        }
        try {
            //Passing null as previous node type to the method signals to other lithium mixins that we only want the neighbor behavior of this block and not its neighbors
            //Using exceptions for control flow, but this way we do not need to copy the code for the cache initialization, reducing required maintenance and improving mod compatibility
            this.pathNodeTypeNeighbor = (WalkNodeEvaluator.checkNeighbourBlocks(new PathfindingContext(singleBlockBlockView, null), 1, 1, 1, null));
            if (this.pathNodeTypeNeighbor == null) {
                this.pathNodeTypeNeighbor = PathType.OPEN;
            }
        } catch (SingleBlockBlockView.SingleBlockViewException | NullPointerException | ClassCastException e) {
            this.pathNodeTypeNeighbor = null;
        }
    }

    @Override
    public PathType lithium$getPathNodeType() {
        return this.pathNodeType;
    }

    @Override
    public PathType lithium$getNeighborPathNodeType() {
        return this.pathNodeTypeNeighbor;
    }

    @Shadow
    protected abstract BlockState asState();

    @Shadow
    public abstract Block getBlock();
}