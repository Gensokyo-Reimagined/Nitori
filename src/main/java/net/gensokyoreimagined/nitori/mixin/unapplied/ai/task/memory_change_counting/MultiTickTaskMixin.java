package net.gensokyoreimagined.nitori.mixin.unapplied.ai.task.memory_change_counting;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.gensokyoreimagined.nitori.common.ai.MemoryModificationCounter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.behavior.Behavior;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Behavior.class)
public class MultiTickTaskMixin<E extends LivingEntity> {
    @Mutable
    @Shadow
    @Final
    protected Map<MemoryModuleType<?>, MemoryStatus> entryCondition;

    @Unique
    private long nitori$cachedMemoryModCount = -1;
    @Unique
    private boolean nitori$cachedHasRequiredMemoryState;

    @Inject(method = "<init>(Ljava/util/Map;II)V", at = @At("RETURN"))
    private void init(Map<MemoryModuleType<?>, MemoryStatus> map, int int_1, int int_2, CallbackInfo ci) {
        this.entryCondition = new Reference2ObjectOpenHashMap<>(map);
    }

    /**
     * @reason Use cached required memory state test result if memory state is unchanged
     * @author 2No2Name
     */
    @Overwrite
    public boolean hasRequiredMemories(E entity) {
        Brain<?> brain = entity.getBrain();
        long modCount = ((MemoryModificationCounter) brain).lithium$getModCount();
        if (this.nitori$cachedMemoryModCount == modCount) {
            return this.nitori$cachedHasRequiredMemoryState;
        }
        this.nitori$cachedMemoryModCount = modCount;

        ObjectIterator<Reference2ObjectMap.Entry<MemoryModuleType<?>, MemoryStatus>> fastIterator = ((Reference2ObjectOpenHashMap<MemoryModuleType<?>, MemoryStatus>) this.entryCondition).reference2ObjectEntrySet().fastIterator();
        while (fastIterator.hasNext()) {
            Reference2ObjectMap.Entry<MemoryModuleType<?>, MemoryStatus> entry = fastIterator.next();
            if (!brain.checkMemory(entry.getKey(), entry.getValue())) {
                return this.nitori$cachedHasRequiredMemoryState = false;
            }
        }

        return this.nitori$cachedHasRequiredMemoryState = true;
    }
}