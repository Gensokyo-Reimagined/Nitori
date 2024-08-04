package net.gensokyoreimagined.nitori.mixin.math.random.creation;

//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.util.RandomSource;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;
//
//@Mixin(ServerPlayer.class)
//public abstract class ServerPlayerEntityRandomMixin {
//
//    @Shadow public abstract ServerLevel serverLevel();
//
//    @Redirect(method = "fudgeSpawnLocation", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;create()Lnet/minecraft/util/RandomSource;"))
//    private RandomSource redirectCreatedRandom() {
//        return serverLevel().random;
//    }
//}