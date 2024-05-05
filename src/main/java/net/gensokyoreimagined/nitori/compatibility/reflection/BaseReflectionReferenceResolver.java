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
package net.gensokyoreimagined.nitori.compatibility.reflection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A resolver for a reflection reference to be invoked at runtime when the resolution should be valid.
 * @param <T> The data type to be returned.
 */
public abstract class BaseReflectionReferenceResolver<T> {
    /**
     * The resolution of this reflection reference. Is null until resolved.
     */
    @Nullable
    private T resolution;

    /**
     * Resolves this reflection reference.
     * @param classLoader The class loader to use for resolving this reflection reference
     * @return The resolution of this reflection reference.
     * @throws ReflectiveOperationException If something went wrong finding the resolution for this reflection reference
     */
    @Nonnull
    protected abstract T resolve(ClassLoader classLoader) throws ReflectiveOperationException;

    /**
     * Accept a classloader and resolve this reflection reference.
     * @param classLoader The class loader to use for resolving this reflection reference
     * @throws ReflectiveOperationException If something went wrong finding the resolution for this reflection reference
     */
    public void accept(ClassLoader classLoader) throws ReflectiveOperationException {
        this.resolution = resolve(classLoader);
    }

    /**
     * Get the resolution of this reflection reference.
     * @return The resolution of this reflection reference.
     * @throws IllegalStateException If this reflection reference has not yet been resolved.
     */
    @Nonnull
    public T getResolution() {
        if (this.resolution == null) {
            throw new IllegalStateException("Reflection reference has not been resolved!");
        }
        return this.resolution;
    }

    /**
     * Common function for resolving a class.
     * @param classLoader The class loader to use for finding the class.
     * @param classPath The path of the class.
     * @return The class
     * @throws ClassNotFoundException If the class could not be found with the given class loader
     */
    @Nonnull
    protected static Class<?> resolveClass(ClassLoader classLoader, String classPath) throws ClassNotFoundException {
        return Class.forName(classPath, false, classLoader);
    }
}
