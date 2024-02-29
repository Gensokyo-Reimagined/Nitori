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
package net.gensokyoreimagined.nitori.core;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import net.minecraft.core.SectionPos;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
 * Originally from CaffeineMC, licensed under GNU Lesser General Public License v3.0
 * See https://github.com/CaffeineMC/lithium-fabric for more information/sources
 */

// 0008-lithium-fast-retrieval.patch
@Mixin(EntitySectionStorage.class)
public class MixinEntitySectionStorage<T extends EntityAccess> {
    @Final
    @Shadow private LongSortedSet sectionIds;

    @Final
    @Shadow private Long2ObjectMap<EntitySection<T>> sections;

    @Inject(method = "forEachAccessibleNonEmptySection", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/SectionPos;posToSectionCoord(D)I", ordinal = 5), cancellable = true)
    public void forEachAccessibleNonEmptySection(AABB box, AbortableIterationConsumer<EntitySection<T>> consumer, CallbackInfo ci, @Local(ordinal = 0) int j, @Local(ordinal = 0) int k, @Local(ordinal = 0) int l, @Local(ordinal = 0) int m, @Local(ordinal = 0) int n, @Local(ordinal = 0) int o) {
        if (m >= j + 4 || o >= l + 4) {
            // Vanilla is likely more optimized when shooting entities with TNT cannons over huge distances.
            // Choosing a cutoff of 4 chunk size, as it becomes more likely that these entity sections do not exist when
            // they are far away from the shot entity (player despawn range, position maybe not on the ground, etc)
            for (int p = j; p <= m; p++) {
                long q = SectionPos.asLong(p, 0, 0);
                long r = SectionPos.asLong(p, -1, -1);
                LongIterator longIterator = this.sectionIds.subSet(q, r + 1L).iterator();

                while (longIterator.hasNext()) {
                    long s = longIterator.nextLong();
                    int t = SectionPos.y(s);
                    int u = SectionPos.z(s);
                    if (t >= k && t <= n && u >= l && u <= o) {
                        EntitySection<T> entitySection = this.sections.get(s);
                        if (entitySection != null && !entitySection.isEmpty() && entitySection.getStatus().isAccessible()) {
                            consumer.accept(entitySection);
                        }
                    }
                }
            }
        } else {
            // Vanilla order of the AVL long set is sorting by ascending long value. The x, y, z positions are packed into
            // a long with the x position's lowest 22 bits placed at the MSB.
            // Therefore the long is negative iff the 22th bit of the x position is set, which happens iff the x position
            // is negative. A positive x position will never have its 22th bit set, as these big coordinates are far outside
            // the world. y and z positions are treated as unsigned when sorting by ascending long value, as their sign bits
            // are placed somewhere inside the packed long
            for (int x = j; x <= m; x++) {
                for (int z = Math.max(l, 0); z <= o; z++) {
                    this.gensouHacks$forEachInColumn(x, k, n, z, consumer);
                }

                int bound = Math.min(-1, o);
                for (int z = l; z <= bound; z++) {
                    this.gensouHacks$forEachInColumn(x, k, n, z, consumer);
                }
            }
        }

        // Cancel the original method
        ci.cancel();
    }

    // Mirai start - lithium: fast retrieval
    @Unique
    private void gensouHacks$forEachInColumn(int x, int k, int n, int z, AbortableIterationConsumer<EntitySection<T>> action) {
        // y from negative to positive, but y is treated as unsigned
        for (int y = Math.max(k, 0); y <= n; y++) {
            this.gensouHacks$consumeSection(SectionPos.asLong(x, y, z), action);
        }
        int bound = Math.min(-1, n);
        for (int y = k; y <= bound; y++) {
            this.gensouHacks$consumeSection(SectionPos.asLong(x, y, z), action);
        }
    }

    @Unique
    private void gensouHacks$consumeSection(long pos, AbortableIterationConsumer<EntitySection<T>> action) {
        EntitySection<T> entitySection = this.getSection(pos);
        if (entitySection != null && !entitySection.isEmpty() && entitySection.getStatus().isAccessible()) {
            action.accept(entitySection);
        }
    }

    @Invoker("getSection")
    private EntitySection<T> getSection(long pos) {
        throw new AssertionError("Mixin failed to apply");
    }
}
