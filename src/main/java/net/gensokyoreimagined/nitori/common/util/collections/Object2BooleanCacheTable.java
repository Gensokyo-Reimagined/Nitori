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
package net.gensokyoreimagined.nitori.common.util.collections;

import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.util.Mth;

import java.util.function.Predicate;

/**
 * A lossy hashtable implementation that stores a mapping between an object and a boolean.
 * <p>
 * Any hash collisions will result in an overwrite: this is safe because the correct value can always be recomputed,
 * given that the given operator is deterministic.
 * <p>
 * This implementation is safe to use from multiple threads
 */
public final class Object2BooleanCacheTable<T> {
    private final int mask;

    private final Node<T>[] nodes;

    private final Predicate<T> operator;

    @SuppressWarnings("unchecked")
    public Object2BooleanCacheTable(int capacity, Predicate<T> operator) {
        int capacity1 = Mth.smallestEncompassingPowerOfTwo(capacity);
        this.mask = capacity1 - 1;

        this.nodes = (Node<T>[]) new Node[capacity1];

        this.operator = operator;
    }

    private static <T> int hash(T key) {
        return HashCommon.mix(key.hashCode());
    }

    public boolean get(T key) {
        int idx = hash(key) & this.mask;

        Node<T> node = this.nodes[idx];
        if (node != null && key.equals(node.key)) {
            return node.value;
        }

        boolean test = this.operator.test(key);
        this.nodes[idx] = new Node<>(key, test);

        return test;
    }

    static class Node<T> {
        final T key;
        final boolean value;

        Node(T key, boolean value) {
            this.key = key;
            this.value = value;
        }
    }
}