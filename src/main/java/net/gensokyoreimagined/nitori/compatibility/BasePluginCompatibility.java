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
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

public abstract class BasePluginCompatibility {

    private final String[] pluginNames;

    private final AtomicBoolean reflectionResolutionAttempted = new AtomicBoolean(false);

    protected BasePluginCompatibility(String[] pluginNames) {
        this.pluginNames = pluginNames;
    }

    @Nullable
    private Map<String, ClassLoader> collectPluginClassLoaders() {
        var pluginManager = Bukkit.getPluginManager();
        HashMap<java.lang.String, java.lang.ClassLoader> pluginClassLoaders = new HashMap<>(this.pluginNames.length, 1.0f);
        for (var pluginName : this.pluginNames) {
            var plugin = pluginManager.getPlugin(pluginName);
            if (plugin == null) {
                return null;
            }
            pluginClassLoaders.put(pluginName, plugin.getClass().getClassLoader());
        }
        return pluginClassLoaders;
    }

    private void onPluginClassesNotFound(ReflectiveOperationException e) {
        //TODO: move repeated text to generalized function
        //TODO: Get mod name for prefix
        LogUtils.getLogger().error(
                "[Nitori] FAILED TO LOCATE CLASSES FOR `" + this.getClass().getName() + "`! I'll assume the related plugin(s) will otherwise hang and always run on main. Please update my intel!\n" +
                        "[Nitori] Exception follows:\n" +
                        e.toString()
        );
    }

    private void onReferenceUseFailure(CompletionException e) {
        //TODO: move repeated text to generalized function
        //TODO: Get mod name for prefix
        LogUtils.getLogger().error(
                "[Nitori] FAILED TO USE REFLECTED REFERENCES FOR `" + this.getClass().getName() + "`! I'll assume the related plugin(s) will otherwise hang and always run on main. Please update my intel!\n" +
                        "[Nitori] Exception follows:\n" +
                        e.toString()
        );
    }

    protected abstract void collectReferences(@Nonnull Map<String, ClassLoader> pluginClassLoaders) throws ReflectiveOperationException;

    boolean completePluginCompatibilityCondition(BooleanSupplier conditionCallback) {
        synchronized (this) { // compatibility classes are singletons so syncing on itself is safe
            if (!this.reflectionResolutionAttempted.getAcquire()) {
                var thisClassName = this.getClass().getName();
                var pluginClassLoaders = collectPluginClassLoaders();
                if (pluginClassLoaders == null) {
                    return false;
                }
                try {
                    //TODO: move repeated text to generalized function
                    //TODO: Get mod name for prefix
                    LogUtils.getLogger().info("[Nitori] Resolving reflection references for " + thisClassName + ".");
                    collectReferences(pluginClassLoaders);
                    //TODO: move repeated text to generalized function
                    //TODO: Get mod name for prefix
                    LogUtils.getLogger().info("[Nitori] Resolved reflection references for " + thisClassName + ".");
                } catch (ReflectiveOperationException e) {
                    onPluginClassesNotFound(e);
                    return true;
                } finally {
                    this.reflectionResolutionAttempted.setRelease(true);
                }
            }
        }
        try {
            return conditionCallback.getAsBoolean();
        } catch (IllegalStateException e) {
            //TODO: move to generalized function
            //TODO: Get mod name for prefix
            LogUtils.getLogger().error("[Nitori] Hey, could you not forget to tell me how to resolve the reflection references??");
            throw e;
        } catch (CompletionException e) {
            onReferenceUseFailure(e);
            return true;
        }
    }
}
