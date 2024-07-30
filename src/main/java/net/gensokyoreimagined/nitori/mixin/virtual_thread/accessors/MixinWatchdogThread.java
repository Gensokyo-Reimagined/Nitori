package net.gensokyoreimagined.nitori.mixin.virtual_thread.accessors;

import org.spigotmc.WatchdogThread;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WatchdogThread.class)
public interface MixinWatchdogThread {
    @Accessor("instance")
    static WatchdogThread getInstance() {
        throw new AssertionError("MixinWatchdogThread was not transformed!");
    }
}
