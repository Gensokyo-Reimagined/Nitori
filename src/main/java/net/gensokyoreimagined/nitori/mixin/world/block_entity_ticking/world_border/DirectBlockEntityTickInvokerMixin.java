package net.gensokyoreimagined.nitori.mixin.world.block_entity_ticking.world_border;

//import net.gensokyoreimagined.nitori.common.world.listeners.WorldBorderListenerOnce;
//import net.minecraft.server.level.FullChunkStatus;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.level.ChunkPos;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.border.WorldBorder;
//import net.minecraft.world.level.border.BorderStatus;
//import net.minecraft.world.level.chunk.LevelChunk;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;
//
//@Mixin(targets = "net.minecraft.world.level.chunk.LevelChunk$BoundTickingBlockEntity")
//public abstract class DirectBlockEntityTickInvokerMixin implements WorldBorderListenerOnce {
//
//    @Shadow
//    @Final
//    LevelChunk LevelChunk;
//
//    @Shadow
//    public abstract BlockPos getPos();
//
//    private byte worldBorderState = 0;
//
//    @Redirect(
//            method = "tick",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/world/chunk/WorldChunk;canTickBlockEntity(Lnet/minecraft/util/math/BlockPos;)Z"
//            )
//    )
//    private boolean cachedCanTickBlockEntity(LevelChunk instance, BlockPos pos) {
//        if (this.isInsideWorldBorder()) {
//            Level world = this.LevelChunk.getLevel();
//            if (world instanceof ServerLevel serverWorld) {
//                return this.LevelChunk.getFullStatus().isOrAfter(FullChunkStatus.BLOCK_TICKING) && serverWorld.areEntitiesLoaded(ChunkPos.asLong(pos));
//            }
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    private boolean isInsideWorldBorder() {
//        if (this.worldBorderState == (byte) 0) {
//            this.startWorldBorderCaching();
//        }
//
//        int worldBorderState = this.worldBorderState;
//        if ((worldBorderState & 3) == 3) {
//            return (worldBorderState & 4) != 0;
//        }
//        return this.LevelChunk.getLevel().getWorldBorder().isWithinBounds(this.getPos());
//    }
//
//    private void startWorldBorderCaching() {
//        this.worldBorderState = (byte) 1;
//        WorldBorder worldBorder = this.LevelChunk.getLevel().getWorldBorder();
//        worldBorder.addListener(this);
//        boolean isStationary = worldBorder.getStatus() == BorderStatus.STATIONARY;
//        if (worldBorder.isWithinBounds(this.getPos())) {
//            if (isStationary || worldBorder.getStatus() == BorderStatus.GROWING) {
//                this.worldBorderState |= (byte) 6;
//            }
//        } else {
//            if (isStationary || worldBorder.getStatus() == BorderStatus.SHRINKING) {
//                this.worldBorderState |= (byte) 2;
//            }
//        }
//    }
//
//    @Override
//    public void lithium$onWorldBorderShapeChange(WorldBorder worldBorder) {
//        this.worldBorderState = (byte) 0;
//    }
//}