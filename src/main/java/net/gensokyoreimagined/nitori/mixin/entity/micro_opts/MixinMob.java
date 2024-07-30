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
package net.gensokyoreimagined.nitori.mixin.entity.micro_opts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MixinMob extends LivingEntity {
    @Unique
    private BlockPos gensouHacks$cachedEyeBlockPos;
    @Unique
    private int gensouHacks$cachedPositionHashcode;

    protected MixinMob(EntityType<? extends LivingEntity> type, Level world) {
        super(type, world);
    }

    @Inject(method = "isSunBurnTick", at = @At("HEAD"), cancellable = true)
    private void isSunBurnTick(CallbackInfoReturnable<Boolean> cir) {
        if (this.level().isDay() && !this.level().isClientSide) {
            int positionHashCode = this.position().hashCode();
            if (this.gensouHacks$cachedPositionHashcode != positionHashCode) {
                this.gensouHacks$cachedEyeBlockPos = new BlockPos((int) this.getX(), (int) this.getEyeY(), (int) this.getZ());
                this.gensouHacks$cachedPositionHashcode = positionHashCode;
            }

            float f = this.level().hasChunkAt(this.getBlockX(), this.getBlockZ()) ? this.level().getLightLevelDependentMagicValue(gensouHacks$cachedEyeBlockPos) : 0.0F;

            // Check brightness first
            if (f <= 0.5F)
            {
                cir.setReturnValue(false);
                cir.cancel();
            }

            if (this.random.nextFloat() * 30.0F >= (f - 0.4F) * 2.0F)
            {
                cir.setReturnValue(false);
                cir.cancel();
            }

            boolean flag = this.isInWaterRainOrBubble() || this.isInPowderSnow || this.wasInPowderSnow;

            if (!flag && this.level().canSeeSky(gensouHacks$cachedEyeBlockPos)) {
                cir.setReturnValue(true);
                cir.cancel();
            }
        }
    }
}
