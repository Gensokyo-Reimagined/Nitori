package net.gensokyoreimagined.nitori.executor.thread;

import io.papermc.paper.util.TickThread;
import net.gensokyoreimagined.nitori.executor.wrapper.MinecraftServerWrapper;
import net.gensokyoreimagined.nitori.mixin.virtual_thread.accessors.MixinMinecraftServer;
import net.gensokyoreimagined.nitori.mixin.virtual_thread.accessors.MixinWatchdogThread;
import net.minecraft.server.MinecraftServer;
import org.gradle.internal.impldep.com.esotericsoftware.kryo.NotNull;
import org.spigotmc.WatchdogThread;

import javax.annotation.Nullable;

/**
 * A {@link TickThread} that provides an implementation for {@link BaseThread},
 * that is shared between the {@link MinecraftServer#serverThread} and {@link WatchdogThread#instance}.
 *
 * @author Martijn Muijsers under AGPL-3.0
 */
public class ServerThread extends TickThread {

    protected ServerThread(final String name) {
        super(name);
    }

    protected ServerThread(final Runnable run, final String name) {
        super(run, name);
    }

    /**
     * This method must not be called while {@link MinecraftServer#isConstructed} is false.
     *
     * @return The global {@link ServerThread} instance, which is either
     * {@link MinecraftServer#serverThread}, or {@link WatchdogThread#instance} while the server is shutting
     * down and the {@link WatchdogThread} was responsible.
     */
    public static @NotNull Thread getInstance() {
        if (((MixinMinecraftServer) MinecraftServerWrapper.SERVER).hasStopped()) {
            if (((MixinMinecraftServer) MinecraftServerWrapper.SERVER).shutdownThread() == MixinWatchdogThread.getInstance()) {
                return MixinWatchdogThread.getInstance();
            }
        }

        return MinecraftServerWrapper.serverThread;
    }

    /**
     * @return The same value as {@link #getInstance()} if {@link MinecraftServer#isConstructed} is true,
     * or null otherwise.
     */
    public static @Nullable Thread getInstanceIfConstructed() {
        return MinecraftServerWrapper.isConstructed ? getInstance() : null;
    }

}
