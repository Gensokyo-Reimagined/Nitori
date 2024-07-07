package net.gensokyoreimagined.nitori.mixin.ai.pathing;

import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Path.class)
public interface PathContextAccessor {

    @Accessor
    BlockPos.MutableBlockPos getLastNodePos();

}