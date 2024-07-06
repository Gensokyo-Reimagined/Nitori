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
package net.gensokyoreimagined.nitori.common.state;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;




/**
 * Many of the column and row key arrays in block state tables will be duplicated, leading to an unnecessary waste of
 * memory. Since we have very limited options for trying to construct more optimized table types without throwing
 * maintainability or mod compatibility out the window, this class acts as a dirty way to find and de-duplicate arrays
 * after we construct our table types.
 * <p>
 * While this global cache does not provide the ability to remove or clear entries from it, the reality is that it
 * shouldn't matter because block state tables are only initialized once and remain loaded for the entire lifetime of
 * the game. Even in the event of classloader pre-boot shenanigans, we still shouldn't leak memory as our cache will be
 * dropped along with the rest of the loaded classes when the class loader is reaped.
 */
public class StatePropertyTableCache {
    public static final FastImmutableTableCache<Property<?>, Comparable<?>, BlockState> BLOCK_STATE_TABLE =
            new FastImmutableTableCache<>();

    public static final FastImmutableTableCache<Property<?>, Comparable<?>, FluidState> FLUID_STATE_TABLE =
            new FastImmutableTableCache<>();

    @SuppressWarnings("unchecked")
    public static <S, O> FastImmutableTableCache<Property<?>, Comparable<?>, S> getTableCache(O owner) {
        if (owner instanceof Block) {
            return (FastImmutableTableCache<Property<?>, Comparable<?>, S>) BLOCK_STATE_TABLE;
        } else if (owner instanceof Fluid) {
            return (FastImmutableTableCache<Property<?>, Comparable<?>, S>) FLUID_STATE_TABLE;
        } else {
            throw new IllegalArgumentException("");
        }
    }
}

