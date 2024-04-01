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

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.papermc.paper.util.maplist.IteratorSafeOrderedReferenceSet;
import io.papermc.paper.world.ChunkEntitySlices;
import net.gensokyoreimagined.nitori.core.MixinIteratorSafeOrderedReferenceSet;
import net.gensokyoreimagined.nitori.core.MixinChunkEntitySlices;
import net.gensokyoreimagined.nitori.core.isolation.net_minecraft_server_level.ChunkMapMixin;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;

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

    private final IteratorSafeOrderedReferenceSet<LevelChunk> entityTickingChunks;
    private final AtomicInteger taskIndex = new AtomicInteger();

    private final ConcurrentLinkedQueue<Runnable> mainThreadTasks;
    private final AtomicInteger finishedTasks = new AtomicInteger();

    public MultithreadedTracker(IteratorSafeOrderedReferenceSet<LevelChunk> entityTickingChunks, ConcurrentLinkedQueue<Runnable> mainThreadTasks) {
        this.entityTickingChunks = entityTickingChunks;
        this.mainThreadTasks = mainThreadTasks;
    }

    public void tick() {
        int iterator = this.entityTickingChunks.createRawIterator();

        if (iterator == -1) {
            return;
        }

        // start with updating players
        try {
            this.taskIndex.set(iterator);
            this.finishedTasks.set(0);

            for (int i = 0; i < parallelism; i++) {
                trackerExecutor.execute(this::runUpdatePlayers);
            }

            while (this.taskIndex.get() < ((MixinIteratorSafeOrderedReferenceSet) (Object) this.entityTickingChunks).getListSize()) {
                this.runMainThreadTasks();
                this.handleChunkUpdates(5); // assist
            }

            while (this.finishedTasks.get() != parallelism) {
                this.runMainThreadTasks();
            }

            this.runMainThreadTasks(); // finish any remaining tasks
        } finally {
            this.entityTickingChunks.finishRawIterator();
        }

        // then send changes
        iterator = this.entityTickingChunks.createRawIterator();

        if (iterator == -1) {
            return;
        }

        try {
            do {
                LevelChunk chunk = this.entityTickingChunks.rawGet(iterator);

                if (chunk != null) {
                    this.updateChunkEntities(chunk, TrackerStage.SEND_CHANGES);
                }
            } while (++iterator < ((MixinIteratorSafeOrderedReferenceSet) (Object) this.entityTickingChunks).getListSize());
        } finally {
            this.entityTickingChunks.finishRawIterator();
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
        while ((index = this.taskIndex.getAndAdd(tasks)) < ((MixinIteratorSafeOrderedReferenceSet) (Object) this.entityTickingChunks).getListSize()) {
            for (int i = index; i < index + tasks && i < ((MixinIteratorSafeOrderedReferenceSet) (Object) this.entityTickingChunks).getListSize(); i++) {
                LevelChunk chunk = this.entityTickingChunks.rawGet(i);
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
        final ChunkEntitySlices entitySlices = chunk.level.getEntityLookup().getChunk(chunk.locX, chunk.locZ);
        if (entitySlices == null) {
            return;
        }

        final Entity[] rawEntities = ((MixinChunkEntitySlices) (Object) entitySlices).entities.getRawData();
        final ChunkMap chunkMap = chunk.level.chunkSource.chunkMap;

        for (int i = 0; i < rawEntities.length; i++) {
            Entity entity = rawEntities[i];
            if (entity != null) {
                ChunkMap.TrackedEntity entityTracker = chunkMap.entityMap.get(entity.getId());
                if (entityTracker != null) {
                    if (trackerStage == TrackerStage.SEND_CHANGES) {
                        entityTracker.serverEntity.sendChanges();
                    } else if (trackerStage == TrackerStage.UPDATE_PLAYERS) {
                        ((ChunkMapMixin.TrackedEntity) (Object) entityTracker).updatePlayers(((ChunkMapMixin.TrackedEntity) (Object) entityTracker).entity.getPlayersInTrackRange());
                    }
                }
            }
        }
    }

}
