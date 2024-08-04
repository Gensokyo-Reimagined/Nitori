package net.gensokyoreimagined.nitori.mixin.util.block_tracking;

//import net.gensokyoreimagined.nitori.common.block.*;
//import net.gensokyoreimagined.nitori.common.entity.block_tracking.ChunkSectionChangeCallback;
//import net.gensokyoreimagined.nitori.common.entity.block_tracking.SectionedBlockChangeTracker;
//import net.minecraft.core.SectionPos;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.chunk.LevelChunkSection;
//import net.minecraft.world.level.chunk.PalettedContainer;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
//
///**
// * Keep track of how many blocks that meet certain criteria are in this chunk section.
// * E.g. if no over-sized blocks are there, collision code can skip a few blocks.
// *
// * @author 2No2Name
// */
//@Mixin(LevelChunkSection.class)
//public abstract class ChunkSectionMixin implements BlockCountingSection, BlockListeningSection {
//
//    @Shadow
//    @Final
//    public PalettedContainer<BlockState> states;
//
//    @Unique
//    private short[] nitori$countsByFlag = null;
//    @Unique
//    private ChunkSectionChangeCallback nitori$changeListener;
//    @Unique
//    private short nitori$listeningMask;
//
//    @Unique
//    private static void nitori$addToFlagCount(short[] countsByFlag, BlockState state, short change) {
//        int flags = ((BlockStateFlagHolder) state).lithium$getAllFlags();
//        int i;
//        while ((i = Integer.numberOfTrailingZeros(flags)) < 32 && i < countsByFlag.length) {
//            //either count up by one (prevFlag not set) or down by one (prevFlag set)
//            countsByFlag[i] += change;
//            flags &= ~(1 << i);
//        }
//    }
//
//    @Override
//    public boolean lithium$mayContainAny(TrackedBlockStatePredicate trackedBlockStatePredicate) {
//        if (this.nitori$countsByFlag == null) {
//            nitori$fastInitClientCounts();
//        }
//        return this.nitori$countsByFlag[trackedBlockStatePredicate.getIndex()] != (short) 0;
//    }
//
//    @Unique
//    private void nitori$fastInitClientCounts() {
//        this.nitori$countsByFlag = new short[BlockStateFlags.NUM_TRACKED_FLAGS];
//        for (TrackedBlockStatePredicate trackedBlockStatePredicate : BlockStateFlags.TRACKED_FLAGS) {
//            if (this.states.maybeHas(trackedBlockStatePredicate)) {
//                //We haven't counted, so we just set the count so high that it never incorrectly reaches 0.
//                //For most situations, this overestimation does not hurt client performance compared to correct counting,
//                this.nitori$countsByFlag[trackedBlockStatePredicate.getIndex()] = 16 * 16 * 16;
//            }
//        }
//    }
//
//    @Inject(method = "recalcBlockCounts()V", at = @At("HEAD"))
//    private void createFlagCounters(CallbackInfo ci) {
//        this.nitori$countsByFlag = new short[BlockStateFlags.NUM_TRACKED_FLAGS];
//    }
//
//    @Inject(
//            method = "read",
//            at = @At(value = "HEAD")
//    )
//    private void resetData(FriendlyByteBuf buf, CallbackInfo ci) {
//        this.nitori$countsByFlag = null;
//    }
//
//    @Inject(
//            method = "setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/world/level/block/state/BlockState;getFluidState()Lnet/minecraft/world/level/material/FluidState;",
//                    ordinal = 0,
//                    shift = At.Shift.BEFORE
//            ),
//            locals = LocalCapture.CAPTURE_FAILHARD
//    )
//    private void updateFlagCounters(int x, int y, int z, BlockState newState, boolean lock, CallbackInfoReturnable<BlockState> cir, BlockState oldState) {
//        this.lithium$trackBlockStateChange(newState, oldState);
//    }
//
//    @Override
//    public void lithium$trackBlockStateChange(BlockState newState, BlockState oldState) {
//        short[] countsByFlag = this.nitori$countsByFlag;
//        if (countsByFlag == null) {
//            return;
//        }
//        int prevFlags = ((BlockStateFlagHolder) oldState).lithium$getAllFlags();
//        int flags = ((BlockStateFlagHolder) newState).lithium$getAllFlags();
//
//        int flagsXOR = prevFlags ^ flags;
//        //we need to iterate over indices that changed or are in the listeningMask
//        //Some Listening Flags are sensitive to both the previous and the new block. Others are only sensitive to
//        //blocks that are different according to the predicate (XOR). For XOR, the block counting needs to be updated
//        //as well.
//        int iterateFlags = (~BlockStateFlags.LISTENING_MASK_OR & flagsXOR) |
//                (BlockStateFlags.LISTENING_MASK_OR & this.nitori$listeningMask & (prevFlags | flags));
//        int flagIndex;
//
//        while ((flagIndex = Integer.numberOfTrailingZeros(iterateFlags)) < 32 && flagIndex < countsByFlag.length) {
//            int flagBit = 1 << flagIndex;
//            //either count up by one (prevFlag not set) or down by one (prevFlag set)
//            if ((flagsXOR & flagBit) != 0) {
//                countsByFlag[flagIndex] += (short) (1 - (((prevFlags >>> flagIndex) & 1) << 1));
//            }
//            if ((this.nitori$listeningMask & flagBit) != 0) {
//                this.nitori$listeningMask = this.nitori$changeListener.onBlockChange(flagIndex, this);
//            }
//            iterateFlags &= ~flagBit;
//        }
//    }
//
//    @Override
//    public void lithium$addToCallback(ListeningBlockStatePredicate blockGroup, SectionedBlockChangeTracker tracker, long sectionPos, Level world) {
//        if (this.nitori$changeListener == null) {
//            if (sectionPos == Long.MIN_VALUE || world == null) {
//                throw new IllegalArgumentException("Expected world and section pos during intialization!");
//            }
//            this.nitori$changeListener = ChunkSectionChangeCallback.create(sectionPos, world);
//        }
//
//        this.nitori$listeningMask = this.nitori$changeListener.addTracker(tracker, blockGroup);
//    }
//
//    @Override
//    public void lithium$removeFromCallback(ListeningBlockStatePredicate blockGroup, SectionedBlockChangeTracker tracker) {
//        if (this.nitori$changeListener != null) {
//            this.nitori$listeningMask = this.nitori$changeListener.removeTracker(tracker, blockGroup);
//        }
//    }
//
//    @Override
//    @Unique
//    public void lithium$invalidateListeningSection(SectionPos sectionPos) {
//        if (this.nitori$listeningMask != 0) {
//            this.nitori$changeListener.onChunkSectionInvalidated(sectionPos);
//            this.nitori$listeningMask = 0;
//        }
//    }
//}
