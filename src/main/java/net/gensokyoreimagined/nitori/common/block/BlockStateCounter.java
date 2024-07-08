package net.gensokyoreimagined.nitori.common.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.material.FluidState;

public class BlockStateCounter implements PalettedContainer.CountConsumer<BlockState> {
    public int nonEmptyBlockCount;
    public int randomTickableBlockCount;
    public int nonEmptyFluidCount;

    @Override
    public void accept(BlockState arg, int i) {
        FluidState lv = arg.getFluidState();
        if (!arg.isAir()) {
            this.nonEmptyBlockCount += i;
            if (arg.isRandomlyTicking()) {
                this.randomTickableBlockCount += i;
            }
        }
        if (!lv.isEmpty()) {
            this.nonEmptyBlockCount += i;
            if (lv.isRandomlyTicking()) {
                this.nonEmptyFluidCount += i;
            }
        }
    }
}
