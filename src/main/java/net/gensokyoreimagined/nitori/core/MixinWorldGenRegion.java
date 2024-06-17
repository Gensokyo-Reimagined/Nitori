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

import net.gensokyoreimagined.nitori.common.util.Pos;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(WorldGenRegion.class)
public class MixinWorldGenRegion {
    @Shadow @Final private ChunkPos firstPos;
    @Shadow @Final private int size;

    @Unique
    private ChunkAccess[] gensouHacks$chunksArr;
    @Unique
    private int gensouHacks$minChunkX;
    @Unique
    private int gensouHacks$minChunkZ;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(ServerLevel world, List<ChunkAccess> chunks, ChunkStatus status, int placementRadius, CallbackInfo ci) {
        this.gensouHacks$minChunkX = this.firstPos.x;
        this.gensouHacks$minChunkZ = this.firstPos.z;
        this.gensouHacks$chunksArr = chunks.toArray(new ChunkAccess[0]);
    }
    
    @Inject(method = "getChunk(II)Lnet/minecraft/world/level/chunk/ChunkAccess;", at = @At("HEAD"), cancellable = true)
    public void getChunk(int chunkX, int chunkZ, CallbackInfoReturnable<ChunkAccess> cir) {
        int x = chunkX - this.gensouHacks$minChunkX;
        int z = chunkZ - this.gensouHacks$minChunkZ;
        int w = this.size;

        if (x >= 0 && z >= 0 && x < w && z < w) {
            cir.setReturnValue(this.gensouHacks$chunksArr[x + z * w]);
            cir.cancel();
        } else {
            throw new NullPointerException("No chunk exists at " + new ChunkPos(chunkX, chunkZ));
        }
    }
    
    @Inject(method = "getBlockState", at = @At("HEAD"), cancellable = true)
    public void getBlockState(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        int x = (Pos.ChunkCoord.fromBlockCoord(pos.getX())) - this.gensouHacks$minChunkX;
        int z = (Pos.ChunkCoord.fromBlockCoord(pos.getZ())) - this.gensouHacks$minChunkZ;
        int w = this.size;

        if (x >= 0 && z >= 0 && x < w && z < w) {
            cir.setReturnValue(this.gensouHacks$chunksArr[x + z * w].getBlockState(pos));
            cir.cancel();
        } else {
            throw new NullPointerException("No chunk exists at " + new ChunkPos(pos));
        }
    }
}
