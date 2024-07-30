package net.gensokyoreimagined.nitori.mixin.math.random.creation;

//import net.gensokyoreimagined.nitori.mixin.math.random.math.GetRandomInterface;
//import net.minecraft.server.rcon.thread.QueryThreadGs4;
//import net.minecraft.server.rcon.thread.QueryThreadGs4.*;
//import net.minecraft.util.RandomSource;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;
//
//// Credit to Mirai patch #0015
//@Mixin(QueryThreadGs4.RequestChallenge.class)
//public class QueryResponseHandlerMixin {
//    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;create()Lnet/minecraft/util/math/random/Random;"))
//    private RandomSource redirectRandomCreation() {
//        return GetRandomInterface.getRandom();
//    }
//}