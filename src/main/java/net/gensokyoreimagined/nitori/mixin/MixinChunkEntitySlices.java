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

import com.destroystokyo.paper.util.maplist.EntityList;
import io.papermc.paper.world.ChunkEntitySlices;
import net.gensokyoreimagined.nitori.access.IMixinChunkEntitySlicesAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkEntitySlices.class)
public abstract class MixinChunkEntitySlices implements IMixinChunkEntitySlicesAccess {
    // Implementation of 0107-Multithreaded-Tracker.patch
    @Override
    @Final
    @Accessor
    public abstract EntityList getEntities();
}
