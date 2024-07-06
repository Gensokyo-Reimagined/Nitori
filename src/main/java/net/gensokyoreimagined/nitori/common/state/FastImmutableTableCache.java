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

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;

public class FastImmutableTableCache<R, C, V> {
    private final ObjectOpenCustomHashSet<R[]> rows;
    private final ObjectOpenCustomHashSet<C[]> columns;
    private final ObjectOpenCustomHashSet<V[]> values;

    private final ObjectOpenCustomHashSet<int[]> indices;

    @SuppressWarnings("unchecked")
    public FastImmutableTableCache() {
        this.rows = new ObjectOpenCustomHashSet<>((Hash.Strategy<R[]>) ObjectArrays.HASH_STRATEGY);
        this.columns = new ObjectOpenCustomHashSet<>((Hash.Strategy<C[]>) ObjectArrays.HASH_STRATEGY);
        this.values = new ObjectOpenCustomHashSet<>((Hash.Strategy<V[]>) ObjectArrays.HASH_STRATEGY);

        this.indices = new ObjectOpenCustomHashSet<>(IntArrays.HASH_STRATEGY);
    }

    public synchronized V[] dedupValues(V[] values) {
        return this.values.addOrGet(values);
    }

    public synchronized R[] dedupRows(R[] rows) {
        return this.rows.addOrGet(rows);
    }

    public synchronized C[] dedupColumns(C[] columns) {
        return this.columns.addOrGet(columns);
    }

    public synchronized int[] dedupIndices(int[] ints) {
        return this.indices.addOrGet(ints);
    }
}