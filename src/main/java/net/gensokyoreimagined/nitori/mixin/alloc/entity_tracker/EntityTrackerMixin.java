//package net.gensokyoreimagined.nitori.mixin.alloc;
//
//import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
//import net.minecraft.server.network.ServerPlayerConnection;
//import net.minecraft.server.level.ChunkMap;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;
//
//import java.util.Set;
//
//@Mixin(ChunkMap.TrackedEntity.class)
//public class EntityTrackerMixin {
//
//    /**
//     * Uses less memory, and will cache the returned iterator.
//     */
//    @Redirect(
//            method = "<init>",
//            require = 0,
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lit/unimi/dsi/fastutil/objects/ReferenceOpenHashSet;<init>()V",
//                    remap = false
//            )
//    )
//    private Set<ServerPlayerConnection> useFasterCollection() {
//        return new ReferenceOpenHashSet<>();
//    }
//}