package net.gensokyoreimagined.nitori.mixin.ai.task.launch;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.gensokyoreimagined.nitori.common.util.collections.MaskedList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.VisibleForDebug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;
import java.util.function.Supplier;

@Mixin(Brain.class)
public class BrainMixin<E extends LivingEntity> {

    @Shadow
    @Final
    private Map<Integer, Map<Activity, Set<BehaviorControl<? super E>>>> availableBehaviorsByPriority;

    @Shadow
    @Final
    private Set<Activity> activeActivities;

    private ArrayList<BehaviorControl<? super E>> nitori$possibleTasks;
    private MaskedList<BehaviorControl<? super E>> nitori$runningTasks;

    private void nitori$onTasksChanged() {
        this.nitori$runningTasks = null;
        this.nitori$onPossibleActivitiesChanged();
    }

    private void nitori$onPossibleActivitiesChanged() {
        this.nitori$possibleTasks = null;
    }

    private void nitori$initPossibleTasks() {
        this.nitori$possibleTasks = new ArrayList<>();
        for (Map<Activity, Set<BehaviorControl<? super E>>> map : this.availableBehaviorsByPriority.values()) {
            for (Map.Entry<Activity, Set<BehaviorControl<? super E>>> entry : map.entrySet()) {
                Activity activity = entry.getKey();
                if (!this.activeActivities.contains(activity)) {
                    continue;
                }
                Set<BehaviorControl<? super E>> set = entry.getValue();
                for (BehaviorControl<? super E> BehaviorControl : set) {
                    //noinspection UseBulkOperation
                    this.nitori$possibleTasks.add(BehaviorControl);
                }
            }
        }
    }

    private ArrayList<BehaviorControl<? super E>> nitori$getPossibleTasks() {
        if (this.nitori$possibleTasks == null) {
            this.nitori$initPossibleTasks();
        }
        return this.nitori$possibleTasks;
    }

    private MaskedList<BehaviorControl<? super E>> nitori$getCurrentlyRunningTasks() {
        if (this.nitori$runningTasks == null) {
            this.nitori$initCurrentlyRunningTasks();
        }
        return this.nitori$runningTasks;
    }

    private void nitori$initCurrentlyRunningTasks() {
        MaskedList<BehaviorControl<? super E>> list = new MaskedList<>(new ObjectArrayList<>(), false);

        for (Map<Activity, Set<BehaviorControl<? super E>>> map : this.availableBehaviorsByPriority.values()) {
            for (Set<BehaviorControl<? super E>> set : map.values()) {
                for (BehaviorControl<? super E> BehaviorControl : set) {
                    list.addOrSet(BehaviorControl, BehaviorControl.getStatus() == Behavior.Status.RUNNING);
                }
            }
        }
        this.nitori$runningTasks = list;
    }

    /**
     * @author 2No2Name
     * @reason use optimized cached collection
     */
    @Overwrite
    private void startEachNonRunningBehavior(ServerLevel level, E entity) {
        long startTime = level.getGameTime();
        for (BehaviorControl<? super E> BehaviorControl : this.nitori$getPossibleTasks()) {
            if (BehaviorControl.getStatus() == Behavior.Status.STOPPED) {
                BehaviorControl.tryStart(level, entity, startTime);
            }
        }
    }

    /**
     * @author 2No2Name
     * @reason use optimized cached collection
     */
    @Overwrite
    @Deprecated
    @VisibleForDebug
    public List<BehaviorControl<? super E>> getRunningBehaviors() {
        return this.nitori$getCurrentlyRunningTasks();
    }


    @Inject(
            method = "<init>(Ljava/util/Collection;Ljava/util/Collection;Lcom/google/common/collect/ImmutableList;Ljava/util/function/Supplier;)V",
            at = @At("RETURN")
    )
    private void reinitializeBrainCollections(Collection<?> memories, Collection<?> sensors, ImmutableList<?> memoryEntries, Supplier<?> codecSupplier, CallbackInfo ci) {
        this.nitori$onTasksChanged();
    }

    @Inject(
            method = "addActivity(Lnet/minecraft/world/entity/schedule/Activity;ILcom/google/common/collect/ImmutableList;)V",
            at = @At("RETURN")
    )
    private void reinitializeTasksSorted(Activity activity, int begin, ImmutableList<? extends BehaviorControl<? super E>> list, CallbackInfo ci) {
        this.nitori$onTasksChanged();
    }

    @Inject(
            method = "clearMemories",
            at = @At("RETURN")
    )
    private void reinitializeTasksSorted(CallbackInfo ci) {
        this.nitori$onTasksChanged();
    }

    @Inject(
            method = "setActiveActivity",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Set;add(Ljava/lang/Object;)Z",
                    shift = At.Shift.AFTER
            )
    )
    private void onPossibleActivitiesChanged(Activity except, CallbackInfo ci) {
        this.nitori$onPossibleActivitiesChanged();
    }


    @Inject(
            method = "stopAll",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/behavior/BehaviorControl;doStop(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;J)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void removeStoppedTask(ServerLevel world, E entity, CallbackInfo ci, long l, Iterator<?> it, BehaviorControl<? super E> task) {
        if (this.nitori$runningTasks != null) {
            this.nitori$runningTasks.setVisible(task, false);
        }
    }

    @Inject(
            method = "tickEachRunningBehavior",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/behavior/BehaviorControl;tickOrStop(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;J)V",
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void removeTaskIfStopped(ServerLevel world, E entity, CallbackInfo ci, long l, Iterator<?> it, BehaviorControl<? super E> BehaviorControl) {
        if (this.nitori$runningTasks != null && BehaviorControl.getStatus() != Behavior.Status.RUNNING) {
            this.nitori$runningTasks.setVisible(BehaviorControl, false);
        }
    }

    @ModifyVariable(
            method = "startEachNonRunningBehavior",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/behavior/BehaviorControl;tryStart(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;J)Z",
                    shift = At.Shift.AFTER
            )
    )
    private BehaviorControl<? super E> addStartedTasks(BehaviorControl<? super E> BehaviorControl) {
        if (this.nitori$runningTasks != null && BehaviorControl.getStatus() == Behavior.Status.RUNNING) {
            this.nitori$runningTasks.setVisible(BehaviorControl, true);
        }
        return BehaviorControl;
    }
}