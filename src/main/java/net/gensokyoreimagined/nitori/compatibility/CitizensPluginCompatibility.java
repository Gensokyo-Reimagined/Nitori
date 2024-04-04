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

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

public class CitizensPluginCompatibility {
    @Unique
    private static final String CITIZENS_PLUGIN_NAME = "Citizens";

    @Nullable
    @Unique
    private static Class<?> citizensPluginCitizensEntityTrackerClass = null;
    @Nullable
    @Unique
    private static Class<?> citizensPluginEntityHumanNPCClass = null;
    @Unique
    private static boolean gensouHacks$attemptedCitizensSearch = false;

    @Unique
    private static void findCitizensClassesIfNeeded(Plugin citizensPlugin) {
        if (!gensouHacks$attemptedCitizensSearch) {
            var citizensPluginClassLoader = citizensPlugin.getClass().getClassLoader();
            gensouHacks$attemptedCitizensSearch = true;
            try {
                citizensPluginCitizensEntityTrackerClass = Class.forName("net.citizensnpcs.nms.v1_20_R3.util.CitizensEntityTracker", false, citizensPluginClassLoader);
                citizensPluginEntityHumanNPCClass = Class.forName("net.citizensnpcs.nms.v1_20_R3.entity.EntityHumanNPC", false, citizensPluginClassLoader);
            } catch (ClassNotFoundException e) {
                //TODO: Get mod name for prefix
                LogUtils.getLogger().error("[Nitori] CITIZENS PLUGIN CLASSES NOT LOCATED! I'll assume Citizens will hang and always run on main. Please update my intel!");
            }
        }
    }

    @Unique
    public static boolean shouldRedirectToMainThread(ChunkMap.TrackedEntity trackedEntity, ServerPlayer serverPlayer) {
        var bukkitPluginManager = Bukkit.getPluginManager();
        if (bukkitPluginManager.isPluginEnabled(CITIZENS_PLUGIN_NAME)) {
            findCitizensClassesIfNeeded(bukkitPluginManager.getPlugin(CITIZENS_PLUGIN_NAME));
            if (citizensPluginCitizensEntityTrackerClass == null || citizensPluginEntityHumanNPCClass == null) {
                return true;
            }
            return citizensPluginCitizensEntityTrackerClass.isInstance(trackedEntity) && !citizensPluginEntityHumanNPCClass.isInstance(serverPlayer);
        }
        return false;
    }
}
