package net.gensokyoreimagined.nitori.mixin.util.accessors;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.Mixin;

// Does not work on Paper...
@Mixin(ServerLevel.class)
public interface ServerWorldAccessor {
    @Accessor
    PersistentEntitySectionManager<Entity> getEntityManager();
}
