package net.gensokyoreimagined.nitori.mixin.unapplied.ai.task.memory_change_counting;

import net.gensokyoreimagined.nitori.common.ai.MemoryModificationCounter;
import net.minecraft.world.entity.ai.Brain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Optional;

@Mixin(Brain.class)
public class BrainMixin implements MemoryModificationCounter {

    private long nitori$memoryModCount = 1;

    @Redirect(
            method = "setMemoryInternal",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private Object increaseMemoryModificationCount(Map<Object, Object> map, Object key, Object newValue) {
        Object oldValue = map.put(key, newValue);
        if (oldValue == null || ((Optional<?>) oldValue).isPresent() != ((Optional<?>) newValue).isPresent()) {
            this.nitori$memoryModCount++;
        }
        return oldValue;
    }

    @Override
    public long lithium$getModCount() {
        return nitori$memoryModCount;
    }


    /**
     * Fix mod count being reset when villager loses profession due to disappearing workstation.
     * Mod count being reset can lead to tasks not running even though they should be!
     */
    @Inject(
            method = "copyWithoutBehaviors",
            at = @At("RETURN")
    )
    private void copyModCount(CallbackInfoReturnable<Brain<?>> cir) {
        Brain<?> newBrain = cir.getReturnValue();
        ((BrainMixin) (Object) newBrain).nitori$memoryModCount = this.nitori$memoryModCount + 1;
    }
}