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

public abstract class BaseReflectionReferenceResolver<T> {
    @Nullable
    private T resolution;

    @Nonnull
    protected abstract T resolve(ClassLoader classLoader) throws ReflectiveOperationException;

    public void accept(ClassLoader classLoader) throws ReflectiveOperationException {
        this.resolution = resolve(classLoader);
    }

    @Nonnull
    public T getResolution() {
        if (this.resolution == null) {
            throw new IllegalStateException("Reflection reference has not been resolved!");
        }
        return this.resolution;
    }

    @Nonnull
    protected static Class<?> resolveClass(ClassLoader classLoader, String classPath) throws ClassNotFoundException {
        return Class.forName(classPath, false, classLoader);
    }
}
