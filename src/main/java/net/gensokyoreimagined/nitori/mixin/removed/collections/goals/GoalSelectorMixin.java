package net.gensokyoreimagined.nitori.mixin.removed.collections.goals;

//import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
//import net.minecraft.world.entity.ai.goal.GoalSelector;
//import net.minecraft.world.entity.ai.goal.WrappedGoal;
//import net.minecraft.util.profiling.ProfilerFiller;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Mutable;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import java.util.Set;
//import java.util.function.Supplier;
//
//@Mixin(GoalSelector.class)
//public abstract class GoalSelectorMixin {
//
//    @Mutable
//    @Shadow
//    @Final
//    private Set<WrappedGoal> availableGoals;
//
//    /**
//     * Replace the goal set with an optimized collection type which performs better for iteration.
//     */
//    @Inject(method = "<init>", at = @At("RETURN"))
//    private void reinit(Supplier<ProfilerFiller> supplier, CallbackInfo ci) {
//        this.availableGoals = new ObjectLinkedOpenHashSet<>(this.availableGoals);
//    }
//}