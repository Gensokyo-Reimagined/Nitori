package net.gensokyoreimagined.nitori.common.world.interests.types;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;

import java.util.Set;
import java.util.function.Predicate;

public class PointOfInterestTypeHelper {
    private static Predicate<BlockState> POI_BLOCKSTATE_PREDICATE;


    public static void init(Set<BlockState> types) {
        if (POI_BLOCKSTATE_PREDICATE != null) {
            throw new IllegalStateException("Already initialized");
        }

        POI_BLOCKSTATE_PREDICATE = types::contains;
    }

    public static boolean mayHavePoi(LevelChunkSection chunkSection) {
        return chunkSection.maybeHas(POI_BLOCKSTATE_PREDICATE);
    }
}