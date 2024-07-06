package net.gensokyoreimagined.nitori.common.world.blockentity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

public interface BlockEntityGetter {
    BlockEntity lithium$getLoadedExistingBlockEntity(BlockPos pos);
}