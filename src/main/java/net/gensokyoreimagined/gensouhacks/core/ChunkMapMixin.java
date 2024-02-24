package net.gensokyoreimagined.gensouhacks.core;

import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.server.level.ChunkMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkMap.class)
public class ChunkMapMixin {

    @Mutable
    @Shadow @Final public Int2ObjectMap<ChunkMap.TrackedEntity> entityMap;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void reassignEntityTrackers(CallbackInfo ci) {
        this.entityMap = new Int2ObjectLinkedOpenHashMap<>();
    }
}
