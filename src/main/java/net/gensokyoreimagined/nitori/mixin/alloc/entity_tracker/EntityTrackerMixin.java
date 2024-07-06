//package net.gensokyoreimagined.nitori.mixin.alloc.entity_tracker;
//
//import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
//import net.minecraft.server.network.ServerPlayerConnection;
//import net.minecraft.server.level.ChunkMap;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;
//
//@Mixin(ChunkMap.TrackedEntity.class)
//public class EntityTrackerMixin {
//
//    /**
//     * Uses less memory and will cache the returned iterator.
//     */
//    @Redirect(
//            method = "<init>",
//            at = @At(
//                    value = "NEW",
//                    target = "it/unimi/dsi/fastutil/objects/ReferenceOpenHashSet",
//                    remap = false
//            )
//    )
//    private ReferenceOpenHashSet<ServerPlayerConnection> useFasterCollection() {
//        return new ReferenceOpenHashSet<>();
//    }
//}

//paper has this