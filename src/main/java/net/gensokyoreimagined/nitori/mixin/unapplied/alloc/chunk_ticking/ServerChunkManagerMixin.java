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
package net.gensokyoreimagined.nitori.mixin.unapplied.alloc.chunk_ticking;

//import net.minecraft.server.level.ChunkHolder;
//import net.minecraft.server.level.ServerChunkCache;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.Redirect;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import java.util.ArrayList;
//import java.util.function.BooleanSupplier;
//
//@Mixin(ServerChunkCache.class)
//public class ServerChunkManagerMixin {
//    private final ArrayList<ChunkHolder> cachedChunkList = new ArrayList<>();
//
//    @Redirect(
//            method = "tickChunks",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lcom/google/common/collect/Lists;newArrayListWithCapacity(I)Ljava/util/ArrayList;",
//                    remap = false
//            )
//    )
//    private ArrayList<ChunkHolder> redirectChunksListClone(int initialArraySize) {
//        ArrayList<ChunkHolder> list = this.cachedChunkList;
//        list.clear(); // Ensure the list is empty before re-using it
//        list.ensureCapacity(initialArraySize);
//
//        return list;
//    }
//
//    @Inject(method = "tick(Ljava/util/function/BooleanSupplier;Z)V", at = @At("HEAD"))
//    private void preTick(BooleanSupplier shouldKeepTicking, boolean tickChunks, CallbackInfo ci) {
//        // Ensure references aren't leaked through this list
//        this.cachedChunkList.clear();
//    }
//}