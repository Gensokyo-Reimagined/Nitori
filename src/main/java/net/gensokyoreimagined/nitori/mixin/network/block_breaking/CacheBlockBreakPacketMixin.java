package net.gensokyoreimagined.nitori.mixin.network.block_breaking;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Credit Mirai patch #0061
@Mixin(ServerLevel.class)
public class CacheBlockBreakPacketMixin {
    @Unique
    private ClientboundBlockDestructionPacket packet;

    @Inject(method = "destroyBlockProgress", at = @At("HEAD"))
    private void setPacketNull(int entityId, BlockPos pos, int progress, CallbackInfo ci) {
        this.packet = null;
    }

    @Redirect(method = "destroyBlockProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"))
    private void redirectPacketSent(ServerGamePacketListenerImpl instance, Packet<?> packetBad, @Local(ordinal = 0, argsOnly = true) int entityId, @Local(ordinal = 0, argsOnly = true) BlockPos pos, @Local(ordinal = 0, argsOnly = true) int progress, @Local(ordinal = 0)ServerPlayer serverPlayerEntity) {
        if (this.packet == null) this.packet = new ClientboundBlockDestructionPacket(entityId, pos, progress);
        serverPlayerEntity.connection.sendPacket(this.packet);
    }
}
