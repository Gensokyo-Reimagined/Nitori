package net.gensokyoreimagined.nitori.mixin.math.rounding;


import net.gensokyoreimagined.nitori.common.math.FasterMathUtil;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public class FastRoundingPlayerMixin {

    @Redirect(
            method = "causeFallDamage",
            require = 0,
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;round(D)J"))
    private long fasterRoundFall(double value) {
        return FasterMathUtil.round(value);
    }
}