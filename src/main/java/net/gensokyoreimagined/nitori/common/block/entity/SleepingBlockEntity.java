package net.gensokyoreimagined.nitori.common.block.entity;

import net.gensokyoreimagined.nitori.mixin.world.block_entity_ticking.sleeping.WrappedBlockEntityTickInvokerAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TickingBlockEntity;

public interface SleepingBlockEntity {
    TickingBlockEntity SLEEPING_BLOCK_ENTITY_TICKER = new TickingBlockEntity() {
        public void tick() {
        }

        public boolean isRemoved() {
            return false;
        }

        public BlockPos getPos() {
            return null;
        }

        public String getType() {
            return "<lithium_sleeping>";
        }
    };

    WrappedBlockEntityTickInvokerAccessor lithium$getTickWrapper();

    void lithium$setTickWrapper(WrappedBlockEntityTickInvokerAccessor tickWrapper);

    TickingBlockEntity lithium$getSleepingTicker();

    void lithium$setSleepingTicker(TickingBlockEntity sleepingTicker);

    default boolean nitori$startSleeping() {
        if (this.isSleeping()) {
            return false;
        }

        WrappedBlockEntityTickInvokerAccessor tickWrapper = this.lithium$getTickWrapper();
        if (tickWrapper == null) {
            return false;
        }
        this.lithium$setSleepingTicker(tickWrapper.getTicker());
        tickWrapper.callRebind(SleepingBlockEntity.SLEEPING_BLOCK_ENTITY_TICKER);
        return true;
    }

    default void sleepOnlyCurrentTick() {
        TickingBlockEntity sleepingTicker = this.lithium$getSleepingTicker();
        WrappedBlockEntityTickInvokerAccessor tickWrapper = this.lithium$getTickWrapper();
        if (sleepingTicker == null) {
            sleepingTicker = tickWrapper.getTicker();
        }
        Level world = ((BlockEntity) this).getLevel();
        tickWrapper.callRebind(new SleepUntilTimeBlockEntityTickInvoker((BlockEntity) this, world.getGameTime() + 1, sleepingTicker));
        this.lithium$setSleepingTicker(null);
    }

    default void wakeUpNow() {
        TickingBlockEntity sleepingTicker = this.lithium$getSleepingTicker();
        if (sleepingTicker == null) {
            return;
        }
        this.setTicker(sleepingTicker);
        this.lithium$setSleepingTicker(null);
    }

    default void setTicker(TickingBlockEntity delegate) {
        WrappedBlockEntityTickInvokerAccessor tickWrapper = this.lithium$getTickWrapper();
        if (tickWrapper == null) {
            return;
        }
        tickWrapper.callRebind(delegate);
    }

    default boolean isSleeping() {
        return this.lithium$getSleepingTicker() != null;
    }
}