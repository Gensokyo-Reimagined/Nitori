package net.gensokyoreimagined.nitori.common.world;

import net.gensokyoreimagined.nitori.common.entity.pushable.BlockCachingEntity;
import net.gensokyoreimagined.nitori.common.entity.pushable.EntityPushablePredicate;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;

public interface ClimbingMobCachingSection {
    AbortableIterationConsumer.Continuation lithium$collectPushableEntities(Level world, Entity except, AABB box, EntityPushablePredicate<? super Entity> entityPushablePredicate, ArrayList<Entity> entities);

    void lithium$onEntityModifiedCachedBlock(BlockCachingEntity entity, BlockState newBlockState);
}
