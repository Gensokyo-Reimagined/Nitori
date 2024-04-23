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

import com.destroystokyo.paper.util.misc.PooledLinkedHashSets;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
//import net.gensokyoreimagined.nitori.config.NitoriConfig;
import net.gensokyoreimagined.nitori.access.IMixinChunkMapAccess;
import net.gensokyoreimagined.nitori.access.IMixinChunkMap_TrackedEntityAccess;
import net.gensokyoreimagined.nitori.compatibility.PluginCompatibilityRegistry;
import net.gensokyoreimagined.nitori.tracker.MultithreadedTracker;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.Nullable;

@Mixin(ChunkMap.class)
public class ChunkMapMixin implements IMixinChunkMapAccess {

    @Mutable
    @Shadow @Final public Int2ObjectMap<ChunkMap.TrackedEntity> entityMap;

    // Implementation of 0107-Multithreaded-Tracker.patch
    @Final
    @Shadow
    public ServerLevel level;

    // Implementation of 0107-Multithreaded-Tracker.patch
    @Unique
    private @Nullable MultithreadedTracker gensouHacks$multithreadedTracker;

    // Implementation of 0107-Multithreaded-Tracker.patch
    @Final
    @Unique
    private final ConcurrentLinkedQueue<Runnable> gensouHacks$trackerMainThreadTasks = new ConcurrentLinkedQueue<>();

    // Implementation of 0107-Multithreaded-Tracker.patch
    @Unique
    private boolean gensouHacks$tracking = false;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void reassignEntityTrackers(CallbackInfo ci) {
        this.entityMap = new Int2ObjectLinkedOpenHashMap<>();
    }

    // Implementation of 0107-Multithreaded-Tracker.patch
    @Override
    @Unique
    public void gensouHacks$runOnTrackerMainThread(final Runnable runnable) {
        if (this.gensouHacks$tracking) {
            this.gensouHacks$trackerMainThreadTasks.add(runnable);
        } else {
            runnable.run();
        }
    }

    @Inject(method = "processTrackQueue", at = @At("HEAD"), cancellable = true)
    private void atProcessTrackQueueHead(CallbackInfo callbackInfo) {
        // Implementation of 0107-Multithreaded-Tracker.patch
        //TODO: Restore config condition
        //if (NitoriConfig.enableAsyncEntityTracker) {
            if (this.gensouHacks$multithreadedTracker == null) {
                this.gensouHacks$multithreadedTracker = new MultithreadedTracker(this.level.chunkSource.entityTickingChunks, this.gensouHacks$trackerMainThreadTasks);
            }

            this.gensouHacks$tracking = true;
            try {
                this.gensouHacks$multithreadedTracker.tick();
            } finally {
                this.gensouHacks$tracking = false;
            }
            callbackInfo.cancel();
        //}
        // Mirai end
    }

    @Mixin(ChunkMap.TrackedEntity.class)
    public static abstract class TrackedEntity implements IMixinChunkMap_TrackedEntityAccess {
        // Implementation of 0107-Multithreaded-Tracker.patch
        @Override
        @Final
        @Accessor
        public abstract Entity getEntity(); // Mirai -> public

        @Final
        @Mutable
        @Shadow
        public Set<ServerPlayerConnection> seenBy;

        @Shadow
        public abstract void updatePlayer(ServerPlayer player);

        @Inject(method = "<init>", at = @At("RETURN"))
        private void reassignSeenBy(CallbackInfo ci) {
            // Implementation of 0107-Multithreaded-Tracker.patch
            this.seenBy = ReferenceSets.synchronize(new ReferenceOpenHashSet<>()); // Mirai - sync
        }

        // Implementation of 0107-Multithreaded-Tracker.patch
        @Override
        @Final
        @Invoker
        public abstract void callUpdatePlayers(PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<ServerPlayer> newTrackerCandidates); // Mirai -> public

        @Redirect(method = "updatePlayers(Lcom/destroystokyo/paper/util/misc/PooledLinkedHashSets$PooledObjectLinkedOpenHashSet;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ChunkMap$TrackedEntity;updatePlayer(Lnet/minecraft/server/level/ServerPlayer;)V"))
        private void handleCitizensPluginTracking(ChunkMap.TrackedEntity self, ServerPlayer serverPlayer) {
            // Nitori - Citizens tracker must run on the main thread to avoid cyclic wait
            if (PluginCompatibilityRegistry.CITIZENS.shouldRedirectToMainThread(self, serverPlayer)) {
                ((IMixinChunkMapAccess) (Object) ((ServerLevel) serverPlayer.level()).chunkSource.chunkMap).gensouHacks$runOnTrackerMainThread(() ->
                    this.updatePlayer(serverPlayer)
                );
            } else {
                this.updatePlayer(serverPlayer);
            }
        }

        // Implementation of 0107-Multithreaded-Tracker.patch
        @SuppressWarnings("EmptyMethod")
        @Redirect(method = "removePlayer", at = @At(value = "INVOKE", target = "Lorg/spigotmc/AsyncCatcher;catchOp(Ljava/lang/String;)V"))
        private void skipSpigotAsyncPlayerTrackerClear(String reason) {} // Mirai - we can remove async too

        // Implementation of 0107-Multithreaded-Tracker.patch
        @SuppressWarnings("EmptyMethod")
        @Redirect(method = "updatePlayer", at = @At(value = "INVOKE", target = "Lorg/spigotmc/AsyncCatcher;catchOp(Ljava/lang/String;)V"))
        private void skipSpigotAsyncPlayerTrackerUpdate(String reason) {} // Mirai - we can update async

        @Redirect(method = "updatePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerEntity;addPairing(Lnet/minecraft/server/level/ServerPlayer;)V"))
        private void handleTrainCartsPluginAddPairing(ServerEntity self, ServerPlayer serverPlayer) {
            if (PluginCompatibilityRegistry.TRAIN_CARTS.shouldRedirectToMainThread((ChunkMap.TrackedEntity) (Object) this)) {
                ((IMixinChunkMapAccess) (Object) ((ServerLevel) serverPlayer.level()).chunkSource.chunkMap).gensouHacks$runOnTrackerMainThread(() ->
                    self.addPairing(serverPlayer)
                );
            } else {
                self.addPairing(serverPlayer);
            }
        }
    }
}
