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
import net.gensokyoreimagined.nitori.compatibility.reflection.FieldReflectionReferenceResolver;
import net.gensokyoreimagined.nitori.compatibility.reflection.MethodReflectionReferenceResolver;
import net.minecraft.server.level.ChunkMap;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.CompletionException;

public final class PluginCompatibilityTrainCarts extends BasePluginCompatibility {
    private static final String BK_COMMON_LIB_PLUGIN_NAME = "BKCommonLib";
    private static final String TRAIN_CARTS_PLUGIN_NAME = "Train_Carts";

    private static final FieldReflectionReferenceResolver bkCommonLibPluginEntityTypingHandler_INSTANCEFieldResolver = new FieldReflectionReferenceResolver(
            "com.bergerkiller.bukkit.common.internal.logic.EntityTypingHandler",
            "INSTANCE"
    );

    private static final MethodReflectionReferenceResolver bkCommonLibPluginEntityTypingHandler_getEntityTrackerEntryHookMethodResolver = new MethodReflectionReferenceResolver(
            "com.bergerkiller.bukkit.common.internal.logic.EntityTypingHandler",
            "getEntityTrackerEntryHook",
            Object.class
    );

    private static final MethodReflectionReferenceResolver bkCommonLibPluginEntityTrackerEntryHook_getControllerMethodResolver = new MethodReflectionReferenceResolver(
            "com.bergerkiller.bukkit.common.internal.hooks.EntityTrackerEntryHook",
            "getController"
    );

    private static final ClassReflectionReferenceResolver trainCartsMinecartMemberNetworkClassResolver = new ClassReflectionReferenceResolver("com.bergerkiller.bukkit.tc.controller.MinecartMemberNetwork");

    PluginCompatibilityTrainCarts() {
        super(new String[]{BK_COMMON_LIB_PLUGIN_NAME, TRAIN_CARTS_PLUGIN_NAME});
    }

    @Override
    protected void collectReferences(@Nonnull Map<String, ClassLoader> pluginClassLoaders) throws ReflectiveOperationException {
        var bkCommonLibClassLoader = pluginClassLoaders.get(BK_COMMON_LIB_PLUGIN_NAME);
        var trainCartsClassLoader = pluginClassLoaders.get(TRAIN_CARTS_PLUGIN_NAME);
        bkCommonLibPluginEntityTypingHandler_INSTANCEFieldResolver.accept(bkCommonLibClassLoader);
        bkCommonLibPluginEntityTypingHandler_getEntityTrackerEntryHookMethodResolver.accept(bkCommonLibClassLoader);
        bkCommonLibPluginEntityTrackerEntryHook_getControllerMethodResolver.accept(bkCommonLibClassLoader);
        trainCartsMinecartMemberNetworkClassResolver.accept(trainCartsClassLoader);
    }

    public boolean shouldRedirectToMainThread(ChunkMap.TrackedEntity trackedEntity) {
        return completePluginCompatibilityCondition(
                () -> {
                    try {
                        /*com.bergerkiller.bukkit.common.internal.logic.EntityTypingHandler*/var bkCommonLibEntityTypingHandlerInstance = bkCommonLibPluginEntityTypingHandler_INSTANCEFieldResolver.getResolution().get(null);
                        /*com.bergerkiller.bukkit.common.internal.hooks.EntityTrackerEntryHook*/var serverEntityBkCommonLibEntityTrackerEntryHook = bkCommonLibPluginEntityTypingHandler_getEntityTrackerEntryHookMethodResolver.getResolution().invoke(bkCommonLibEntityTypingHandlerInstance, trackedEntity);
                        if (serverEntityBkCommonLibEntityTrackerEntryHook == null) {
                            return false;
                        }
                        /*com.bergerkiller.bukkit.common.controller.EntityNetworkController*/var serverEntityBkCommonLibUncastedNetworkController = bkCommonLibPluginEntityTrackerEntryHook_getControllerMethodResolver.getResolution().invoke(serverEntityBkCommonLibEntityTrackerEntryHook);
                        return trainCartsMinecartMemberNetworkClassResolver.getResolution().isInstance(serverEntityBkCommonLibUncastedNetworkController);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new CompletionException(e);
                    }
                }
        );
    }
}
