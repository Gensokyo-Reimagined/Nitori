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
package net.gensokyoreimagined.nitori.mixin.removed;

//import com.google.common.collect.Lists;
//import net.gensokyoreimagined.nitori.common.util.collections.HashedReferenceList;
//import net.gensokyoreimagined.nitori.common.world.ChunkRandomSource;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.TickingBlockEntity;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Mutable;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import java.util.List;
//
//@Mixin(Level.class)
//public class MixinLevel implements ChunkRandomSource {
//    // Implementation of 0006-lithium-HashedReferenceList.patch
//    @Mutable
//    @Final @Shadow
//    public List<TickingBlockEntity> blockEntityTickers;
//
//    // Implementation of 0006-lithium-HashedReferenceList.patch
//    @Mutable
//    @Final @Shadow
//    private List<TickingBlockEntity> pendingBlockEntityTickers;
//
//    @Shadow
//    protected int randValue;
//
//    // Implementation of 0006-lithium-HashedReferenceList.patch
//    @Inject(method = "<init>", at = @At("RETURN"))
//    private void onInit(CallbackInfo ci) {
//        this.blockEntityTickers = new HashedReferenceList<>(Lists.newArrayList());
//        this.pendingBlockEntityTickers = new HashedReferenceList<>(Lists.newArrayList());
//    }
//
//    @Override
//    public void nitori$getRandomPosInChunk(int x, int y, int z, int mask, BlockPos.MutableBlockPos out) {
//        this.randValue = this.randValue * 3 + 1013904223;
//        int rand = this.randValue >> 2;
//        out.set(x + (rand & 15), y + (rand >> 16 & mask), z + (rand >> 8 & 15));
//    }
//}
