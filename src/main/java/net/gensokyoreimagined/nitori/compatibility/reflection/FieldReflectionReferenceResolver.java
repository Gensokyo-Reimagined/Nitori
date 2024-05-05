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

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class FieldReflectionReferenceResolver extends BaseReflectionReferenceResolver<Field> {

    private final String classPath;

    private final String fieldName;

    /**
     * Creates a new field resolver.
     * @param classPath The path of the class containing the field.
     * @param fieldName the name of the field.
     */
    public FieldReflectionReferenceResolver(String classPath, String fieldName) {
        this.classPath = classPath;
        this.fieldName = fieldName;
    }

    @Override
    @NotNull
    protected Field resolve(ClassLoader classLoader) throws ClassNotFoundException, NoSuchFieldException {
        return resolveClass(classLoader, this.classPath).getField(this.fieldName);
    }
}
