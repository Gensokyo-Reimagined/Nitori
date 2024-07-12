package net.gensokyoreimagined.nitori.access;

import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.PlayerMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.util.thread.BlockableEventLoop;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerChunkCache.class)
public interface IThreadedAnvilChunkStorage {

    @Invoker
    ChunkHolder invokeGetCurrentChunkHolder(long pos);

    @Invoker
    ChunkHolder invokeGetChunkHolder(long pos);

    @Accessor
    ServerLevel getLevel();

    @Accessor
    PlayerMap getPlayerChunkWatchingManager();

    @Accessor
    BlockableEventLoop<Runnable> getMainThreadExecutor();

}