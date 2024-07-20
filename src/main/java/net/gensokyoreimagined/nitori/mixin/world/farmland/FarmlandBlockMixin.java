package net.gensokyoreimagined.nitori.mixin.world.farmland;

import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * Credit to PaperMC patch #0682 and EinS4ckZwiebeln
 */
@Mixin(FarmBlock.class)
public abstract class FarmlandBlockMixin {

    /**
     * @author QPCrummer
     * @reason Optimize FarlandBlock nearby water lookup
     */
    @Overwrite
    private static boolean isNearWater(LevelReader world, BlockPos pos) {
        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();

        for (int dz = -4; dz <= 4; ++dz) {
            int z = dz + posZ;
            for (int dx = -4; dx <= 4; ++dx) {
                int x = posX + dx;
                for (int dy = 0; dy <= 1; ++dy) {
                    net.minecraft.world.level.chunk.LevelChunk chunk = (LevelChunk) world.getChunk(x >> 4, z >> 4);
                    net.minecraft.world.level.material.FluidState fluid = chunk.getBlockState(new BlockPos(x, dy + posY, z)).getFluidState();
                    if (fluid.is(FluidTags.WATER)) return true;
                }
            }
        }
        return false;
    }
}