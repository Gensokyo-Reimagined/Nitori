package net.gensokyoreimagined.nitori.common.block;

import net.gensokyoreimagined.nitori.common.entity.block_tracking.SectionedBlockChangeTracker;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;

public interface BlockListeningSection {

    void lithium$addToCallback(ListeningBlockStatePredicate blockGroup, SectionedBlockChangeTracker tracker, long sectionPos, Level world);

    void lithium$removeFromCallback(ListeningBlockStatePredicate blockGroup, SectionedBlockChangeTracker tracker);

    void lithium$invalidateListeningSection(SectionPos sectionPos);
}