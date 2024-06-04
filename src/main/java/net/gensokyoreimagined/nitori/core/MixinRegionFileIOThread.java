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

import io.papermc.paper.chunk.system.io.RegionFileIOThread;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(io.papermc.paper.chunk.system.io.RegionFileIOThread.class)
public class MixinRegionFileIOThread {
    // What if we didn't care about the region file calculation?
    @Redirect(method = "loadDataAsyncInternal", at = @At(value = "FIELD", target = "Lio/papermc/paper/chunk/system/io/RegionFileIOThread$ImmediateCallbackCompletion;regionFileCalculation:Ljava/lang/Boolean;", opcode = Opcodes.GETFIELD))
    private Boolean trickSkipCheck(@Coerce Object ignored) {
        return Boolean.TRUE;
    }

    // What if we didn't need to set the region file calculation?
    @Redirect(method = "loadDataAsyncInternal", at = @At(value = "FIELD", target = "Lio/papermc/paper/chunk/system/io/RegionFileIOThread$ImmediateCallbackCompletion;needsRegionFileTest:Z", opcode = Opcodes.GETFIELD))
    private boolean trickSkipCheck2(@Coerce Object ignored) {
        return false;
    }

    // What if we didn't check if the region file exists?
    @Redirect(method = "loadDataAsyncInternal", at = @At(value = "INVOKE", target = "Lio/papermc/paper/chunk/system/io/RegionFileIOThread;doesRegionFileExist(IIZLio/papermc/paper/chunk/system/io/RegionFileIOThread$ChunkDataController;)Ljava/lang/Boolean;"))
    private Boolean trickSkipCheck3(int chunkX, int chunkZ, boolean intendingToBlock, RegionFileIOThread.ChunkDataController taskController) {
        return Boolean.TRUE;
    }
}
