package net.gensokyoreimagined.nitori.mixin.world.block_entity_ticking;

import com.google.common.collect.Lists;
import net.gensokyoreimagined.nitori.common.util.collections.HashedReferenceList;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Level.class)
public class HashedReferenceListMixin {
    // Implementation of 0006-lithium-HashedReferenceList.patch
    @Mutable
    @Final @Shadow
    public List<TickingBlockEntity> blockEntityTickers;

    // Implementation of 0006-lithium-HashedReferenceList.patch
    @Mutable
    @Final @Shadow
    private List<TickingBlockEntity> pendingBlockEntityTickers;

    // Implementation of 0006-lithium-HashedReferenceList.patch
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        this.blockEntityTickers = new HashedReferenceList<>(Lists.newArrayList());
        this.pendingBlockEntityTickers = new HashedReferenceList<>(Lists.newArrayList());
    }
}