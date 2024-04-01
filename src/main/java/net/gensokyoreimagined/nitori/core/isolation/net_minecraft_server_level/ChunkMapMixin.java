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
package net.gensokyoreimagined.nitori.core.isolation.net_minecraft_server_level;

import com.destroystokyo.paper.util.misc.PooledLinkedHashSets;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import net.gensokyoreimagined.nitori.plugins.NitoriConfig;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.Nullable;

@Mixin(ChunkMap.class)
public class ChunkMapMixin {

    @Mutable
    @Shadow @Final public Int2ObjectMap<ChunkMap.TrackedEntity> entityMap;

    // Implementation of 0107-Multithreaded-Tracker.patch
    @Shadow @Final
    ServerLevel level;

    // Implementation of 0107-Multithreaded-Tracker.patch
    @Unique
    private @Nullable MultithreadedTracker multithreadedTracker;

    // Implementation of 0107-Multithreaded-Tracker.patch
    @Unique @Final
    private final ConcurrentLinkedQueue<Runnable> trackerMainThreadTasks = new ConcurrentLinkedQueue<>();

    // Implementation of 0107-Multithreaded-Tracker.patch
    @Unique
    private boolean tracking = false;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void reassignEntityTrackers(CallbackInfo ci) {
        this.entityMap = new Int2ObjectLinkedOpenHashMap<>();
    }

    // Implementation of 0107-Multithreaded-Tracker.patch
    @Unique
    public void runOnTrackerMainThread(final Runnable runnable) {
        if (this.tracking) {
            this.trackerMainThreadTasks.add(runnable);
        } else {
            runnable.run();
        }
    }

    @Inject(method = "processTrackQueue", at = @At("HEAD"))
    private void atProcessTrackQueueHead(CallbackInfo callbackInfo) {
        // Implementation of 0107-Multithreaded-Tracker.patch
        //if (NitoriConfig.enableAsyncEntityTracker) {
        if (true) {
            if (this.multithreadedTracker == null) {
                this.multithreadedTracker = new MultithreadedTracker(this.level.chunkSource.entityTickingChunks, this.trackerMainThreadTasks);
            }

            this.tracking = true;
            try {
                this.multithreadedTracker.tick();
            } finally {
                this.tracking = false;
            }
            callbackInfo.cancel();
        }
        // Mirai end
    }

    @Mixin(ChunkMap.TrackedEntity.class)
    public abstract class TrackedEntity {
        // Implementation of 0107-Multithreaded-Tracker.patch
        @Shadow @Final
        public ServerEntity serverEntity; // Mirai -> public

        // Implementation of 0107-Multithreaded-Tracker.patch
        @Shadow @Final
        public Entity entity; // Mirai -> public

        @Mutable @Shadow @Final
        public Set<ServerPlayerConnection> seenBy;

        @Inject(method = "<init>", at = @At("RETURN"))
        private void reassignSeenBy(CallbackInfo ci) {
            // Implementation of 0107-Multithreaded-Tracker.patch
            this.seenBy = ReferenceSets.synchronize(new ReferenceOpenHashSet<>()); // Mirai - sync
        }

        // Implementation of 0107-Multithreaded-Tracker.patch
        @Shadow
        public final void updatePlayers(PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<ServerPlayer> newTrackerCandidates) { // Mirai -> public
            throw new AssertionError("Mixin failed to apply!");
        }

        // Implementation of 0107-Multithreaded-Tracker.patch
        @Redirect(method = "removePlayer", at = @At(value = "INVOKE", target = "Lorg/spigotmc/AsyncCatcher;catchOp(Ljava/lang/String;)V"))
        private void skipSpigotAsyncPlayerTrackerClear(String reason) {} // Mirai - we can remove async too

        // Implementation of 0107-Multithreaded-Tracker.patch
        @Redirect(method = "updatePlayer", at = @At(value = "INVOKE", target = "Lorg/spigotmc/AsyncCatcher;catchOp(Ljava/lang/String;)V"))
        private void skipSpigotAsyncPlayerTrackerUpdate(String reason) {} // Mirai - we can update async
    }
}
