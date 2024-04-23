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

import java.lang.reflect.Method;

public class MethodReflectionReferenceResolver extends BaseReflectionReferenceResolver<Method> {

    private final String classPath;

    private final String methodName;

    private final Class<?>[] methodParameterClasses;

    public MethodReflectionReferenceResolver(String classPath, String methodName, Class<?>... methodParameterClasses) {
        this.classPath = classPath;
        this.methodName = methodName;
        this.methodParameterClasses = methodParameterClasses;
    }

    @Override
    @NotNull
    protected Method resolve(ClassLoader classLoader) throws ClassNotFoundException, NoSuchMethodException {
        return resolveClass(classLoader, this.classPath).getMethod(this.methodName, this.methodParameterClasses);
    }
}
