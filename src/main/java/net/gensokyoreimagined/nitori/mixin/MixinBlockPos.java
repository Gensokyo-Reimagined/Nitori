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
package net.gensokyoreimagined.nitori.mixin;

import it.unimi.dsi.fastutil.longs.LongList;
import net.gensokyoreimagined.nitori.cached_blockpos_iteration.IterateOutwardsCache;
import net.gensokyoreimagined.nitori.cached_blockpos_iteration.LongList2BlockPosMutableIterable;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.gensokyoreimagined.nitori.cached_blockpos_iteration.IterateOutwardsCache.POS_ZERO;

@Mixin(BlockPos.class)
public class MixinBlockPos {
    // Implementation of 0068-lithium-cache-iterate-outwards.patch
    private static final IterateOutwardsCache ITERATE_OUTWARDS_CACHE = new IterateOutwardsCache(50);
    // Implementation of 0068-lithium-cache-iterate-outwards.patch
    private static final LongList HOGLIN_PIGLIN_CACHE = ITERATE_OUTWARDS_CACHE.getOrCompute(8, 4, 8);

    // Implementation of 0068-lithium-cache-iterate-outwards.patch
    @Inject(method = "withinManhattan", at = @At("HEAD"), cancellable = true)
    private static void withinManhattan(BlockPos center, int rangeX, int rangeY, int rangeZ, CallbackInfoReturnable<Iterable<BlockPos>> cir) {
        if (center != POS_ZERO) {
            final LongList positions = rangeX == 8 && rangeY == 4 && rangeZ == 8 ? HOGLIN_PIGLIN_CACHE : ITERATE_OUTWARDS_CACHE.getOrCompute(rangeX, rangeY, rangeZ);
            cir.setReturnValue(new LongList2BlockPosMutableIterable(center, positions));
            cir.cancel();
        }
    }
}
