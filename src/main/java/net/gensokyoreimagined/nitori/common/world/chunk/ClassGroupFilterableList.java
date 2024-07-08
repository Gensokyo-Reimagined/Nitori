package net.gensokyoreimagined.nitori.common.world.chunk;

import net.gensokyoreimagined.nitori.common.entity.EntityClassGroup;
import java.util.Collection;

public interface ClassGroupFilterableList<T> {
    Collection<T> lithium$getAllOfGroupType(EntityClassGroup type);
}
