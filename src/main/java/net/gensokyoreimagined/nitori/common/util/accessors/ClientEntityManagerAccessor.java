package net.gensokyoreimagined.nitori.common.util.accessors;

import net.minecraft.world.level.entity.TransientEntitySectionManager;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySectionStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TransientEntitySectionManager.class)
public interface ClientEntityManagerAccessor<T extends EntityAccess> {
    @Accessor
    EntitySectionStorage<T> getCache();
}