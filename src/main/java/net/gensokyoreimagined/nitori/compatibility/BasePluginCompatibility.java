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

/**
 * Base class for plugin compatibility. Defines common functions for evaluating plugin compatibility conditions.
 * Any given instance of a subclass should always be a singleton. See {@link net.gensokyoreimagined.nitori.compatibility.PluginCompatibilityRegistry}
 */
public abstract class BasePluginCompatibility {

    /**
     * List of plugin names required by this compatibility class. The names in this list are to be the same used in
     * the plugin.yml files in their respective plugins.
     */
    private final String[] pluginNames;

    /**
     * Whether the compatibility class has attempted to resolve the reflection references yet or not.
     */
    private boolean reflectionResolutionAttempted = false;

    /**
     * Define a new compatibility class.
     * @param pluginNames See {@link net.gensokyoreimagined.nitori.compatibility.BasePluginCompatibility#pluginNames}
     */
    protected BasePluginCompatibility(String[] pluginNames) {
        this.pluginNames = pluginNames;
    }

    /**
     * Finds the class loaders for the plugins needed for this compatibility class.
     * @return The class loaders for the plugins needed for this compatibility class.
     */
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

    /**
     * Logs a message for when checking a compatibility condition fails.
     * @param problemFormat The format String to be used for explaining the problem. Requires one `%s` for the class name.
     * @param e The exception thrown by the check failure.
     */
    private void logCompatibilityCheckFailureMessage(@Nonnull String problemFormat, @Nonnull Exception e) {
        NitoriUtil.getPreferredLogger().error(NitoriUtil.makeLogMessage(problemFormat.formatted("`" + this.getClass().getName() + "`") + "! I'll assume the related plugin(s) will otherwise hang and always run on main. Please update my intel! Exception follows:\n" +
                e));
    }

    /**
     * What to do when the plugin classes are not found.
     * @param e The exception thrown by the plugin classes not being found.
     */
    private void onPluginClassesNotFound(ReflectiveOperationException e) {
        logCompatibilityCheckFailureMessage("FAILED TO LOCATE CLASSES FOR %s", e);
    }

    /**
     * What to do when attempting to use the plugin reflection references fails.
     * @param e The exception thrown by an attempt to use a plugin reflection reference fails.
     */
    private void onReferenceUseFailure(CompletionException e) {
        logCompatibilityCheckFailureMessage("FAILED TO USE REFLECTED REFERENCES FOR %s!", e);
    }

    /**
     * Resolves and caches plugin reflection references.
     * @param pluginClassLoaders The plugin class loaders used to resolve the reflection references.
     * @throws ReflectiveOperationException If something went wrong finding the resolutions for reflection references.
     */
    protected abstract void collectReferences(@Nonnull Map<String, ClassLoader> pluginClassLoaders) throws ReflectiveOperationException;

    /**
     * Fullfills a plugin compatibility boolean condition.
     * @param conditionCallback The function used to ultimately determine if the condition passes or fails.
     *                          The condition callback may throw an {@link IllegalStateException} if the reflection references have not yet been resolved.
     * @return If the necessary plugins aren't loaded, false.
     *         Otherwise, if the reflection references failed to be resolved, or an unexpected exception is thrown by the condition callback, true.
     *         Otherwise, the result of the condition callback.
     * @throws IllegalStateException If the reflection references weren't resolved before calling the condition callback.
     */
    boolean completePluginCompatibilityCondition(BooleanSupplier conditionCallback) {
        synchronized (this) { // compatibility classes are singletons so syncing on itself is safe
            if (!this.reflectionResolutionAttempted) {
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
                    this.reflectionResolutionAttempted = true;
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
