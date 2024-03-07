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

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;



@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow private Level level;
    @Shadow public abstract int getBlockX();
    @Shadow public abstract double getEyeY();
    @Shadow public abstract int getBlockZ();
    @Shadow private Vec3 position;

    /**
     * Implementation of 0065-some-entity-micro-opts.patch
     */
    @Unique
    public float gensouHacks$getLightLevelDependentMagicValue(BlockPos pos) {
        return this.level.hasChunkAt(this.getBlockX(), this.getBlockZ()) ? this.level.getLightLevelDependentMagicValue(pos) : 0.0F;
    }

    /**
     * Implementation of 0065-some-entity-micro-opts.patch
     */
    @Inject(method = "getLightLevelDependentMagicValue", at = @At("HEAD"), cancellable = true)
    private void getLightLevelDependentMagicValue(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(this.gensouHacks$getLightLevelDependentMagicValue(new BlockPos(this.getBlockX(), (int) this.getEyeY(), this.getBlockZ())));
        cir.cancel();
    }
}
