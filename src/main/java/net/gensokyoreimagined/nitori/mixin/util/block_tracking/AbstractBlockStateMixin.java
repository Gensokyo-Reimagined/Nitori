package net.gensokyoreimagined.nitori.mixin.util.block_tracking;

import net.gensokyoreimagined.nitori.common.block.BlockStateFlagHolder;
import net.gensokyoreimagined.nitori.common.block.BlockStateFlags;
import net.gensokyoreimagined.nitori.common.block.TrackedBlockStatePredicate;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class AbstractBlockStateMixin implements BlockStateFlagHolder {
    @Unique
    private int flags;

    @Inject(method = "initCache", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        this.initFlags();
    }

    @Unique
    private void initFlags() {
        TrackedBlockStatePredicate.FULLY_INITIALIZED.set(true);

        int flags = 0;

        for (int i = 0; i < BlockStateFlags.FLAGS.length; i++) {
            //noinspection ConstantConditions
            if (BlockStateFlags.FLAGS[i].test((BlockState) (Object) this)) {
                flags |= 1 << i;
            }
        }

        this.flags = flags;
    }

    @Override
    public int lithium$getAllFlags() {
        return this.flags;
    }
}
