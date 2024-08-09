package net.gensokyoreimagined.nitori.mixin.alloc.chunk_random;

//import net.gensokyoreimagined.nitori.common.world.ChunkRandomSource;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.core.BlockPos;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;
//
//@Mixin(ServerLevel.class)
//public abstract class ServerWorldMixin {
//    private final BlockPos.MutableBlockPos nitori$randomPosInChunkCachedPos = new BlockPos.MutableBlockPos();
//
//    /**
//     * @reason Avoid allocating BlockPos every invocation through using our allocation-free variant
//     */
//    @Redirect(
//            method = "tickChunk",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/server/level/ServerLevel;getBlockRandomPos(IIII)Lnet/minecraft/core/BlockPos;"
//            )
//    )
//    private BlockPos redirectTickGetRandomPosInChunk(ServerLevel serverWorld, int x, int y, int z, int mask) {
//        ((ChunkRandomSource) serverWorld).nitori$getRandomPosInChunk(x, y, z, mask, this.nitori$randomPosInChunkCachedPos);
//
//        return this.nitori$randomPosInChunkCachedPos;
//    }
//
//    /**
//     * @reason Ensure an immutable block position is passed on block tick
//     */
//    @Redirect(
//            method = "tickChunk",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/world/level/block/state/BlockState;randomTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V"
//            )
//    )
//    private void redirectBlockStateTick(BlockState blockState, ServerLevel world, BlockPos pos, net.minecraft.util.RandomSource rand) {
//        blockState.randomTick(world, pos.immutable(), rand);
//    }

//    /**
//     * @reason Ensure an immutable block position is passed on fluid tick
//     */
//    @Redirect(
//            method = "tickChunk",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/fluid/FluidState;onRandomTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V"
//            )
//    )
//    private void redirectFluidStateTick(FluidState fluidState, ServerLevel world, BlockPos pos, net.minecraft.util.RandomSource rand) {
//        fluidState.randomTick(world, pos.immutable(), rand);
//    }
//}

// couldn't figure it out....