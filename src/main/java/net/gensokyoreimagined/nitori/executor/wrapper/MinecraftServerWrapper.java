package net.gensokyoreimagined.nitori.executor.wrapper;

import net.minecraft.server.MinecraftServer;

public class MinecraftServerWrapper {
    // Named to make replacement of Gale patches easier (replace MinecraftServer.serverThread with MinecraftServerWrapper.serverThread)
    public static Thread serverThread;
    public static MinecraftServer SERVER;
    public static boolean isConstructed;
}
