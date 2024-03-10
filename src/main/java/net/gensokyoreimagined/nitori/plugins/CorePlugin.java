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
package net.gensokyoreimagined.nitori.plugins;

import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class CorePlugin implements IMixinConfigPlugin {
  @Override
  public void onLoad(final @NotNull String mixinPackage) {
  }

  @Override
  public @Nullable String getRefMapperConfig() {
      return null;
  }

  @Override
  public boolean shouldApplyMixin(final @NotNull String targetClassName, final @NotNull String mixinClassName) {
    return true;
  }

  @Override
  public void acceptTargets(final @NotNull Set<String> myTargets, final @NotNull Set<String> otherTargets) {
  }

  @Override
  public @Nullable List<String> getMixins() {
      return null;
  }

  @Override
  public void preApply(final @NotNull String targetClassName, final @NotNull ClassNode targetClass, final @NotNull String mixinClassName, final @NotNull IMixinInfo mixinInfo) {
  }

  @Override
  public void postApply(final @NotNull String targetClassName, final @NotNull ClassNode targetClass, final @NotNull String mixinClassName, final @NotNull IMixinInfo mixinInfo) {
  }
}
