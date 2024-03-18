// Nitori Copyright (C) 2024 Gensokyo Reimagined
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
package net.gensokyoreimagined.nitori.cached_blockpos_iteration;

/*
 * Originally from CaffeineMC, licensed under GNU Lesser General Public License v3.0
 * See https://github.com/CaffeineMC/lithium-fabric for more information/sources
 */

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * @author 2No2Name
 */
public class LongList2BlockPosMutableIterable implements Iterable<BlockPos> {

    private final LongList positions;
    private final int xOffset, yOffset, zOffset;

    public LongList2BlockPosMutableIterable(BlockPos offset, LongList posList) {
        this.xOffset = offset.getX();
        this.yOffset = offset.getY();
        this.zOffset = offset.getZ();
        this.positions = posList;
    }

    @Override
    public @NotNull Iterator<BlockPos> iterator() {
        return new Iterator<>() {

            private final LongIterator it = LongList2BlockPosMutableIterable.this.positions.iterator();
            private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public net.minecraft.core.BlockPos next() {
                long nextPos = this.it.nextLong();
                return this.pos.set(
                        LongList2BlockPosMutableIterable.this.xOffset + BlockPos.getX(nextPos),
                        LongList2BlockPosMutableIterable.this.yOffset + BlockPos.getY(nextPos),
                        LongList2BlockPosMutableIterable.this.zOffset + BlockPos.getZ(nextPos));
            }
        };
    }
}
