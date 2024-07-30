package net.gensokyoreimagined.nitori.mixin.virtual_thread.accessors;

//import com.llamalad7.mixinextras.sugar.Local;
//import net.gensokyoreimagined.nitori.executor.thread.OriginalServerThread;
//import net.gensokyoreimagined.nitori.executor.wrapper.MinecraftServerWrapper;
//import net.minecraft.server.MinecraftServer;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.gen.Accessor;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(MinecraftServer.class)
//public interface MixinMinecraftServer {
//    @Inject(method = "<init>", at = @At("RETURN"))
//    private void onConstructed(CallbackInfo ci, @Local(argsOnly = true) Thread thread) {
//        MinecraftServerWrapper.SERVER = (MinecraftServer) this;
//        MinecraftServerWrapper.isConstructed = true;
//
//        if (thread instanceof OriginalServerThread) {
//            MinecraftServerWrapper.serverThread = (OriginalServerThread) thread;
//            return;
//        }
//
//        throw new AssertionError("Type of serverThread is not OriginalServerThread!");
//    }
//
//    @Accessor("hasStopped")
//    boolean hasStopped();
//
//    @Accessor("shutdownThread")
//    Thread shutdownThread();
//
//    @Accessor("")
//}
