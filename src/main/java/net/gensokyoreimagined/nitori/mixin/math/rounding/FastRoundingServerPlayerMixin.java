package net.gensokyoreimagined.nitori.mixin.math.rounding;

import net.gensokyoreimagined.nitori.common.math.FasterMathUtil;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public class FastRoundingServerPlayerMixin {
    @Redirect(
            method = "checkMovementStatistics",
            require = 0,
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;round(D)J"))
    private long fasterRound(double value) {
        return FasterMathUtil.round(value);
    }
}