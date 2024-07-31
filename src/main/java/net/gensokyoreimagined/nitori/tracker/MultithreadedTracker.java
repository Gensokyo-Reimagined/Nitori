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
package net.gensokyoreimagined.nitori.tracker;

/*
 * Ported from Petal, derived from Airplane
 */

import ca.spottedleaf.moonrise.common.list.ReferenceList;
import ca.spottedleaf.moonrise.common.misc.NearbyPlayers;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import ca.spottedleaf.moonrise.common.list.IteratorSafeOrderedReferenceSet; //io.papermc.paper.util.maplist.IteratorSafeOrderedReferenceSet;
import ca.spottedleaf.moonrise.patches.chunk_system.level.entity.ChunkEntitySlices; //io.papermc.paper.world.ChunkEntitySlices;
import net.gensokyoreimagined.nitori.access.IMixinChunkEntitySlicesAccess;
import net.gensokyoreimagined.nitori.access.IMixinChunkMap_TrackedEntityAccess;
import net.gensokyoreimagined.nitori.access.IMixinIteratorSafeOrderedReferenceSetAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MultithreadedTracker {

    private enum TrackerStage {
        UPDATE_PLAYERS,
        SEND_CHANGES
    }

    private static final int parallelism = Math.max(4, Runtime.getRuntime().availableProcessors());
    private static final Executor trackerExecutor = Executors.newFixedThreadPool(parallelism, new ThreadFactoryBuilder()
            .setNameFormat("mirai-tracker-%d")
            .setPriority(Thread.NORM_PRIORITY - 2)
            .build());

    private final ReferenceList<ServerChunkCache.ChunkAndHolder> entityTickingChunks;
    private final AtomicInteger taskIndex = new AtomicInteger();

    private final ConcurrentLinkedQueue<Runnable> mainThreadTasks;
    private final AtomicInteger finishedTasks = new AtomicInteger();

    public MultithreadedTracker(ReferenceList<ServerChunkCache.ChunkAndHolder> entityTickingChunks, ConcurrentLinkedQueue<Runnable> mainThreadTasks) {
        this.entityTickingChunks = entityTickingChunks;
        this.mainThreadTasks = mainThreadTasks;
    }

    public void tick() {
        this.taskIndex.set(0);
        this.finishedTasks.set(0);

        for (int i = 0; i < parallelism; i++) {
            trackerExecutor.execute(this::runUpdatePlayers);
        }

        // start with updating players
        while (this.taskIndex.get() < this.entityTickingChunks.size()) {
            this.runMainThreadTasks();
            this.handleChunkUpdates(5); // assist
        }

        // then send changes
        while (this.finishedTasks.get() != parallelism) {
            this.runMainThreadTasks();
        }

        this.runMainThreadTasks(); // finish any remaining tasks

        for (ServerChunkCache.ChunkAndHolder chunkAndHolder : this.entityTickingChunks) {
            LevelChunk chunk = chunkAndHolder.chunk();

            if (chunk != null) {
                this.updateChunkEntities(chunk, TrackerStage.SEND_CHANGES);
            }
        }
    }

    private void runMainThreadTasks() {
        try {
            Runnable task;
            while ((task = this.mainThreadTasks.poll()) != null) {
                task.run();
            }
        } catch (Throwable throwable) {
            MinecraftServer.LOGGER.warn("Tasks failed while ticking track queue", throwable);
        }
    }

    private void runUpdatePlayers() {
        try {
            while (handleChunkUpdates(10));
        } finally {
            this.finishedTasks.incrementAndGet();
        }
    }

    private boolean handleChunkUpdates(int tasks) {
        int index;
        while ((index = this.taskIndex.getAndAdd(tasks)) < this.entityTickingChunks.size()) {
            for (int i = index; i < index + tasks && i < this.entityTickingChunks.size(); i++) {
                LevelChunk chunk = this.entityTickingChunks.getChecked(i).chunk();
                if (chunk != null) {
                    try {
                        this.updateChunkEntities(chunk, TrackerStage.UPDATE_PLAYERS);
                    } catch (Throwable throwable) {
                        MinecraftServer.LOGGER.warn("Ticking tracker failed", throwable);
                    }

                }
            }

            return true;
        }

        return false;
    }

    private void updateChunkEntities(LevelChunk chunk, TrackerStage trackerStage) {
        final ChunkEntitySlices entitySlices = chunk.level.moonrise$getEntityLookup().getChunk(chunk.locX, chunk.locZ);
        if (entitySlices == null) {
            return;
        }

        final Entity[] rawEntities = ((IMixinChunkEntitySlicesAccess) (Object) entitySlices).getEntities().getRawData();
        final ChunkMap chunkMap = chunk.level.chunkSource.chunkMap;

        for (int i = 0; i < rawEntities.length; i++) {
            Entity entity = rawEntities[i];
            if (entity != null) {
                ChunkMap.TrackedEntity entityTracker = chunkMap.entityMap.get(entity.getId());
                if (entityTracker != null) {
                    if (trackerStage == TrackerStage.SEND_CHANGES) {
                        entityTracker.serverEntity.sendChanges();
                    } else if (trackerStage == TrackerStage.UPDATE_PLAYERS) {
                        ReferenceList<ServerPlayer> nearbyPlayers = chunkMap.level.moonrise$getNearbyPlayers().getChunk(entity.chunkPosition()).getPlayers(NearbyPlayers.NearbyMapType.VIEW_DISTANCE);
                        List<ServerPlayer> nearbyPlayersList = new ArrayList<ServerPlayer>(nearbyPlayers.size()); // intentionally typed as List for requirement clarity
                        nearbyPlayers.forEach(nearbyPlayersList::add);
                        ((IMixinChunkMap_TrackedEntityAccess) (Object) entityTracker).callUpdatePlayers(nearbyPlayersList);
                    }
                }
            }
        }
    }

}
