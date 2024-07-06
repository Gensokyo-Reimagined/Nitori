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
package net.gensokyoreimagined.nitori.mixin.alloc.blockstate;

import com.google.common.collect.Table;
import net.gensokyoreimagined.nitori.common.state.FastImmutableTable;
import net.gensokyoreimagined.nitori.common.state.StatePropertyTableCache;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(StateHolder.class)
public class StateMixin<O, S> {
    @Shadow
    private Table<Property<?>, Comparable<?>, S> neighbours;

    @Shadow
    @Final
    protected O owner;

    @Inject(method = "populateNeighbours", at = @At("RETURN"))
    private void postCreateWithTable(Map<Map<Property<?>, Comparable<?>>, S> states, CallbackInfo ci) {
        if (this.owner instanceof Block || this.owner instanceof Fluid) {
            this.neighbours = new FastImmutableTable<Property<?>, Comparable<?>, S>(this.neighbours, StatePropertyTableCache.getTableCache(this.owner));
        }
    }

}