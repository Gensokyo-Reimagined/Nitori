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

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
 * Originally from CaffeineMC, licensed under GNU Lesser General Public License v3.0
 * See https://github.com/CaffeineMC/lithium-fabric for more information/sources
 */

@Mixin(Block.BlockStatePairKey.class)
public class MixinBlock {
    @Unique
    private int gensouHacks$hash;

    @Shadow
    @Final
    private BlockState first;

    @Shadow
    @Final
    private BlockState second;

    @Shadow
    @Final
    private Direction direction;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo info) {
        int hash = first.hashCode();
        hash = 31 * hash + second.hashCode();
        hash = 31 * hash + direction.hashCode();
        this.gensouHacks$hash = hash;
    }

    /**
     * @author DoggySazHi
     * @reason Implementation of 0045-lithium-cached-hashcode.patch
     */
    @Overwrite
    public int hashCode() {
        return this.gensouHacks$hash;
    }
}
