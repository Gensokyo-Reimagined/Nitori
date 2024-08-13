package net.gensokyoreimagined.nitori.mixin.world.block_entity_ticking.sleeping.campfire.lit;

import net.gensokyoreimagined.nitori.common.block.entity.SleepingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin extends BlockEntity implements SleepingBlockEntity {

    public CampfireBlockEntityMixin(BlockPos pos, BlockState state) {
        super(BlockEntityType.CAMPFIRE, pos, state);
    }

    @Inject(
            method = "cookTick",
            at = @At("RETURN"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void trySleepLit(Level world, BlockPos pos, BlockState state, CampfireBlockEntity campfire, CallbackInfo ci, boolean flag) {
        if (!flag) {
            CampfireBlockEntityMixin self = (CampfireBlockEntityMixin) (Object) campfire;
            self.nitori$startSleeping();
        }
    }
}
