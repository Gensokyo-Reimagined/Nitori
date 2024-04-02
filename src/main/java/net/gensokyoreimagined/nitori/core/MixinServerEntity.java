// Nitori Copyright (C) 2024 Gensokyo Reimagined
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
package net.gensokyoreimagined.nitori.core;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.sugar.Local;
import net.gensokyoreimagined.nitori.access.IMixinChunkMapAccess;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Mixin(ServerEntity.class)
public abstract class MixinServerEntity {
    @Final
    @Shadow
    private Entity entity;

    @Shadow
    @Nullable
    private List<SynchedEntityData.DataValue<?>> trackedDataValues;

    @Shadow
    public void sendPairingData(ServerPlayer serverplayer, Consumer<Packet<ClientGamePacketListener>> consumer) {
        throw new AssertionError("Mixin failed to apply!");
    }

    // Implementation of 0107-Multithreaded-Tracker.patch
    @SuppressWarnings("EmptyMethod")
    @Redirect(method = "removePairing", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"))
    private void skipSendForOriginalRemovePairing(ServerGamePacketListenerImpl self, Packet<?> packet) {}

    // Implementation of 0107-Multithreaded-Tracker.patch
    @Inject(method = "removePairing", at = @At(value = "TAIL"))
    private void invokeRemovePairingSendOnMain(ServerPlayer serverplayer, CallbackInfo callbackInfo) {
        // Mirai start - ensure main thread
        ((IMixinChunkMapAccess) (Object) ((ServerLevel) this.entity.level()).chunkSource.chunkMap).gensouHacks$runOnTrackerMainThread(() ->
            serverplayer.connection.send(new ClientboundRemoveEntitiesPacket(this.entity.getId()))
        );
        // Mirai end
    }

    // Implementation of 0107-Multithreaded-Tracker.patch
    @SuppressWarnings("EmptyMethod")
    @Redirect(method = "addPairing", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerEntity;sendPairingData(Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V"))
    private void skipSendPrepForOriginalAddPairing(ServerEntity self, ServerPlayer serverplayer, Consumer<Packet<ClientGamePacketListener>> consumer) {}

    // Implementation of 0107-Multithreaded-Tracker.patch
    @SuppressWarnings("EmptyMethod")
    @Redirect(method = "addPairing", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"))
    private void skipSendForOriginalAddPairing(ServerGamePacketListenerImpl self, Packet<?> packet) {}

    // Implementation of 0107-Multithreaded-Tracker.patch
    @Inject(method = "addPairing", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "FIELD", target = "Lnet/minecraft/server/level/ServerEntity;entity:Lnet/minecraft/world/entity/Entity;", opcode = Opcodes.GETFIELD, shift = At.Shift.BEFORE))
    private void invokeAddPairingSendOnMain(ServerPlayer serverplayer, CallbackInfo callbackInfo, @Local List<Packet<ClientGamePacketListener>> list) {
        ((IMixinChunkMapAccess) (Object) ((ServerLevel) this.entity.level()).chunkSource.chunkMap).gensouHacks$runOnTrackerMainThread(() -> { // Mirai - main thread
            this.sendPairingData(serverplayer, list::add);
            serverplayer.connection.send(new ClientboundBundlePacket(list));
        });
    }

    // Implementation of 0107-Multithreaded-Tracker.patch
    @SuppressWarnings("SameReturnValue")
    @Redirect(method = "sendDirtyEntityData", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData;getNonDefaultValues()Ljava/util/List;"))
    private List<SynchedEntityData.DataValue<?>> skipGetForGenericNonDefault(SynchedEntityData self) {return null;}

    // Implementation of 0107-Multithreaded-Tracker.patch
    @SuppressWarnings("EmptyMethod")
    @Redirect(method = "sendDirtyEntityData", at = @At(value = "FIELD", target = "Lnet/minecraft/server/level/ServerEntity;trackedDataValues:Ljava/util/List;", opcode = Opcodes.PUTFIELD))
    private void skipSetForGenericNonDefault(ServerEntity self, List<SynchedEntityData.DataValue<?>> nonDefaultValues) {}

    // Implementation of 0107-Multithreaded-Tracker.patch
    @SuppressWarnings("EmptyMethod")
    @Redirect(method = "sendDirtyEntityData", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerEntity;broadcastAndSend(Lnet/minecraft/network/protocol/Packet;)V"))
    private void skipTrasmitForNonDefault(ServerEntity self, Packet<?> packet) {}

    // Implementation of 0107-Multithreaded-Tracker.patch
    @Inject(method = "sendDirtyEntityData", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "FIELD", target = "Lnet/minecraft/server/level/ServerEntity;entity:Lnet/minecraft/world/entity/Entity;", opcode = Opcodes.GETFIELD, ordinal = 2, shift = At.Shift.BY, by = -4))
    private void invokeSendForGenericDirtyEntityDataOnMain(CallbackInfo callbackInfo, @Local SynchedEntityData synchedentitydata, @Local List<SynchedEntityData.DataValue<?>> list) {
        // Mirai start - sync
        ((IMixinChunkMapAccess) (Object) ((ServerLevel) this.entity.level()).chunkSource.chunkMap).gensouHacks$runOnTrackerMainThread(() -> {
            this.trackedDataValues = synchedentitydata.getNonDefaultValues();
            this.broadcastAndSend(new ClientboundSetEntityDataPacket(this.entity.getId(), list));
        });
        // Mirai end
    }

    // stubbing of broadcastAndSend in if (this.entity instanceof LivingEntity) handled in skipTrasmitForNonDefault

    // Implementation of 0107-Multithreaded-Tracker.patch
    @Inject(method = "sendDirtyEntityData", at = @At(value = "INVOKE", target = "Ljava/util/Set;clear()V", shift = At.Shift.BY, by = -4))
    private void invokeSendForLivingDirtyEntityDataOnMain(CallbackInfo callbackInfo, @Local Set<AttributeInstance> set) {
        // Mirai start - sync
        final var copy = Lists.newArrayList(set);
        ((IMixinChunkMapAccess) (Object) ((ServerLevel) this.entity.level()).chunkSource.chunkMap).gensouHacks$runOnTrackerMainThread(() -> {
            // CraftBukkit start - Send scaled max health
            if (this.entity instanceof ServerPlayer) {
                ((ServerPlayer) this.entity).getBukkitEntity().injectScaledMaxHealth(copy, false);
            }
            // CraftBukkit end
            this.broadcastAndSend(new ClientboundUpdateAttributesPacket(this.entity.getId(), copy));
        });
        // Mirai end
    }    

    @Shadow
    private void broadcastAndSend(Packet<?> packet) {
        throw new AssertionError("Mixin failed to apply!");
    }
}
