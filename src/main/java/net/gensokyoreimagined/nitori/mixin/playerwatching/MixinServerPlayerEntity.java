package net.gensokyoreimagined.nitori.mixin.playerwatching;

import net.gensokyoreimagined.nitori.common.chunkwatching.PlayerClientVDTracking;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class MixinServerPlayerEntity implements PlayerClientVDTracking {

    @Unique
    private boolean vdChanged = false;

    @Unique
    private int clientVD = 2;

    @Inject(method = "updateOptions", at = @At("HEAD"))
    private void onClientSettingsChanged(ClientInformation packet, CallbackInfo ci) {
        final int currentVD = packet.viewDistance();
        if (currentVD != this.clientVD) this.vdChanged = true;
        this.clientVD = Math.max(2, currentVD);
    }

    @Inject(method = "restoreFrom", at = @At("RETURN"))
    private void onPlayerCopy(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) {
        this.clientVD = ((PlayerClientVDTracking) oldPlayer).getClientViewDistance();
        this.vdChanged = true;
    }

    @Unique
    @Override
    public boolean isClientViewDistanceChanged() {
        return this.vdChanged;
    }

    @Unique
    @Override
    public int getClientViewDistance() {
        this.vdChanged = false;
        return this.clientVD;
    }
}