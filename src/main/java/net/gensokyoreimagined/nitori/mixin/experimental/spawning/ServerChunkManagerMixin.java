package net.gensokyoreimagined.nitori.mixin.experimental.spawning;

//import net.gensokyoreimagined.nitori.common.world.ChunkAwareEntityIterable;
//import net.gensokyoreimagined.nitori.mixin.util.accessors.ServerEntityManagerAccessor;
//import net.gensokyoreimagined.nitori.mixin.util.accessors.ServerWorldAccessor;
//import net.minecraft.server.level.ServerChunkCache;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.entity.Entity;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;
//
//@Mixin(ServerChunkCache.class)
//public class ServerChunkManagerMixin {
//
//    @Redirect(
//            method = "tickChunks",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/server/level/ServerLevel;getAllEntities()Ljava/lang/Iterable;"
//            )
//    )
//    private Iterable<Entity> iterateEntitiesChunkAware(ServerLevel serverLevel) {
//        //noinspection unchecked
//        return ((ChunkAwareEntityIterable<Entity>) ((ServerEntityManagerAccessor<Entity>) ((ServerWorldAccessor) serverLevel).getEntityManager()).getSectionStorage()).lithium$IterateEntitiesInTrackedSections();
//    }
//}

//PersistentEntitySectionManager does not work on paper