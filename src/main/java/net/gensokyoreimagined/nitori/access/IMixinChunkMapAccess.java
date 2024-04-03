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
package net.gensokyoreimagined.nitori.access;

import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

public interface IMixinChunkMapAccess {
    void gensouHacks$runOnTrackerMainThread(final Runnable runnable);

    @Nullable
    Class<?> gensouHacks$citizensPluginTrackedEntityClass = initCitizensPluginTrackedEntityClass();
    @Nullable
    Class<?> gensouHacks$citizensPluginHumanNPCEntityClass = initCitizensPluginHumanNPCEntityClass();

    @Nullable
    private static Class<?> initCitizensPluginTrackedEntityClass() {
        try {
            return Class.forName("net.citizensnpcs.nms.v1_20_R3.util.CitizensEntityTracker", false, ClassLoader.getSystemClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    @Nullable
    private static Class<?> initCitizensPluginHumanNPCEntityClass() {
        try {
            return Class.forName("net.citizensnpcs.nms.v1_20_R3.entity.EntityHumanNPC", false, ClassLoader.getSystemClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Nullable
    static Class<?> getGensouHacks$citizensPluginTrackedEntityClass() {
        return gensouHacks$citizensPluginTrackedEntityClass;
    }
    @Nullable
    static Class<?> getGensouHacks$citizensPluginHumanNPCEntityClass() {
        return gensouHacks$citizensPluginHumanNPCEntityClass;
    }
}
