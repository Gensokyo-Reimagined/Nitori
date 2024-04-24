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

import net.gensokyoreimagined.nitori.util.NitoriUtil;
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

    private void logCompatibilityCheckFailureMessage(@Nonnull String problemFormat, @Nonnull Exception e) {
        NitoriUtil.getPreferredLogger().error(NitoriUtil.makeLogMessage(problemFormat.formatted("`" + this.getClass().getName() + "`") + "! I'll assume the related plugin(s) will otherwise hang and always run on main. Please update my intel! Exception follows:\n" +
                e));
    }

    private void onPluginClassesNotFound(ReflectiveOperationException e) {
        logCompatibilityCheckFailureMessage("FAILED TO LOCATE CLASSES FOR %s", e);
    }

    private void onReferenceUseFailure(CompletionException e) {
        logCompatibilityCheckFailureMessage("FAILED TO USE REFLECTED REFERENCES FOR %s!", e);
    }

    protected abstract void collectReferences(@Nonnull Map<String, ClassLoader> pluginClassLoaders) throws ReflectiveOperationException;

    boolean completePluginCompatibilityCondition(BooleanSupplier conditionCallback) {
        synchronized (this) { // compatibility classes are singletons so syncing on itself is safe
            if (!this.reflectionResolutionAttempted.getAcquire()) {
                var pluginClassLoaders = collectPluginClassLoaders();
                if (pluginClassLoaders == null) {
                    return false;
                }
                var thisClassName = this.getClass().getSimpleName();
                NitoriUtil.getPreferredLogger().info(NitoriUtil.makeLogMessage("Resolving reflection references for " + thisClassName + "."));
                try {
                    collectReferences(pluginClassLoaders);
                } catch (ReflectiveOperationException e) {
                    onPluginClassesNotFound(e);
                    return true;
                } finally {
                    this.reflectionResolutionAttempted.setRelease(true);
                }
                NitoriUtil.getPreferredLogger().info(NitoriUtil.makeLogMessage("Resolved reflection references for " + thisClassName + "."));
            }
        }
        try {
            return conditionCallback.getAsBoolean();
        } catch (IllegalStateException e) {
            NitoriUtil.getPreferredLogger().error(NitoriUtil.makeLogMessage("Hey, could you not forget to inform me on how to resolve the reflection references??"));
            throw e;
        } catch (CompletionException e) {
            onReferenceUseFailure(e);
            return true;
        }
    }
}
