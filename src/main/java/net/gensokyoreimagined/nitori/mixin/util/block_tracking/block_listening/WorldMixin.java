package net.gensokyoreimagined.nitori.mixin.util.block_tracking.block_listening;

import net.gensokyoreimagined.nitori.common.entity.block_tracking.SectionedBlockChangeTracker;
import net.gensokyoreimagined.nitori.common.util.deduplication.LithiumInterner;
import net.gensokyoreimagined.nitori.common.util.deduplication.LithiumInternerWrapper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Level.class)
public class WorldMixin implements LithiumInternerWrapper<SectionedBlockChangeTracker> {
    private final LithiumInterner<SectionedBlockChangeTracker> blockChangeTrackers = new LithiumInterner<>();

    @Override
    public SectionedBlockChangeTracker lithium$getCanonical(SectionedBlockChangeTracker value) {
        return this.blockChangeTrackers.getCanonical(value);
    }

    @Override
    public void lithium$deleteCanonical(SectionedBlockChangeTracker value) {
        this.blockChangeTrackers.deleteCanonical(value);
    }
}