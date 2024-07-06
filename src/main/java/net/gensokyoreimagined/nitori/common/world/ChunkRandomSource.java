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
package net.gensokyoreimagined.nitori.common.world;

import net.minecraft.core.BlockPos;

// Taken from Lithium
// https://github.com/CaffeineMC/lithium-fabric/blob/427dd75ffc922cc1858c1db4b283cc54744567e0/src/main/java/me/jellysquid/mods/lithium/common/world/ChunkRandomSource.java

public interface ChunkRandomSource {
    /**
     * Alternative implementation of {@link net.minecraft.server.level.ServerLevel#getBlockRandomPos(int, int, int, int)} which does not allocate
     * a new {@link BlockPos}.
     */
    void nitori$getRandomPosInChunk(int x, int y, int z, int mask, BlockPos.MutableBlockPos out);
}
