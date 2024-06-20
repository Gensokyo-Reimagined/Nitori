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
package net.gensokyoreimagined.nitori.compatibility;

import net.gensokyoreimagined.nitori.compatibility.reflection.ClassReflectionReferenceResolver;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nonnull;
import java.util.Map;

public final class PluginCompatibilityCitizens extends BasePluginCompatibility {
    private static final String CITIZENS_PLUGIN_NAME = "Citizens";

    private static final ClassReflectionReferenceResolver citizensPluginCitizensEntityTrackerClassResolver = new ClassReflectionReferenceResolver("net.citizensnpcs.nms.v1_20_R4.util.CitizensEntityTracker");

    private static final ClassReflectionReferenceResolver citizensPluginEntityHumanNPCClassResolver = new ClassReflectionReferenceResolver("net.citizensnpcs.nms.v1_20_R4.entity.EntityHumanNPC");

    PluginCompatibilityCitizens() {
        super(new String[]{CITIZENS_PLUGIN_NAME});
    }

    @Override
    protected void collectReferences(@Nonnull Map<String, ClassLoader> pluginClassLoaders) throws ReflectiveOperationException {
        var citizensClassLoader = pluginClassLoaders.get(CITIZENS_PLUGIN_NAME);
        citizensPluginCitizensEntityTrackerClassResolver.accept(citizensClassLoader);
        citizensPluginEntityHumanNPCClassResolver.accept(citizensClassLoader);
    }

    public boolean shouldRedirectToMainThread(ChunkMap.TrackedEntity trackedEntity, ServerPlayer serverPlayer) {
        return completePluginCompatibilityCondition(
                () -> citizensPluginCitizensEntityTrackerClassResolver.getResolution().isInstance(trackedEntity)
                        && !citizensPluginEntityHumanNPCClassResolver.getResolution().isInstance(serverPlayer)
        );
    }
}
