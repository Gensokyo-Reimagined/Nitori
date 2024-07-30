package net.gensokyoreimagined.nitori.executor.thread;

//import net.gensokyoreimagined.nitori.executor.wrapper.MinecraftServerWrapper;
//
///**
// * An abstract base class implementing {@link AbstractYieldingThread},
// * that provides implementation that is common between
// * {@link TickThread} and {@link AssistThread}.
// *
// * @author Martijn Muijsers under AGPL-3.0
// */
//public abstract class BaseThread extends Thread implements AbstractYieldingThread {
//
//    /**
//     * The minimum time to wait as the {@link MinecraftServer#serverThread} when performing a timed wait.
//     * Given in nanoseconds.
//     * If a timed wait with a lower time is attempted, the wait is not performed at all.
//     */
//    public static final long SERVER_THREAD_WAIT_NANOS_MINIMUM = 10_000;
//
//    /**
//     * The time to wait as the {@link MinecraftServer#serverThread} during the oversleep phase, if
//     * there may be delayed tasks.
//     * Given in nanoseconds.
//     */
//    public static final long SERVER_THREAD_WAIT_NANOS_DURING_OVERSLEEP_WITH_DELAYED_TASKS = 50_000;
//
//    /**
//     * The index of this thread, as needed as an argument to
//     * {@link BaseThreadPool#getThreadByBaseIndex(int)}.
//     */
//    public final int baseThreadIndex;
//
//    /**
//     * The maximum yield depth for this thread,
//     * which equals 1 for a {@link ServerThread}
//     * and {@link AssistThread#MAXIMUM_YIELD_DEPTH} for an {@link AssistThread}.
//     */
//    public final int maximumYieldDepth;
//
//    /**
//     * The number of times this thread holds a {@link YieldingLock},
//     * used in {@link #holdsYieldingLock()}.
//     *
//     * @see AbstractYieldingThread#incrementHeldYieldingLockCount()
//     */
//    @ThisThreadOnly
//    public int heldYieldingLockCount = 0;
//
//    /**
//     * The current yield depth of this thread.
//     */
//    @AnyThreadSafe(Access.READ) @ThisThreadOnly(Access.WRITE)
//    public volatile int yieldDepth = 0;
//
//    /**
//     * Whether this thread can currently start yielding tasks with respect to being restricted
//     * due to {@link #yieldDepth} being at least {@link #maximumYieldDepth}.
//     * <br>
//     * This is updated using {@link #updateCanStartYieldingTasks()}
//     * after {@link #yieldDepth} or {@link #heldYieldingLockCount} is changed.
//     */
//    @AnyThreadSafe(Access.READ) @ThisThreadOnly(Access.WRITE)
//    public volatile boolean canStartYieldingTasks = true;
//
//    /**
//     * The highest {@link BaseTaskQueueTier} of any task on the yielding execution stack of this thread,
//     * or null if there is no task being executed on this thread.
//     */
//    @AnyThreadSafe(Access.READ) @ThisThreadOnly(Access.WRITE)
//    public volatile @Nullable BaseTaskQueueTier highestTierOfTaskOnStack;
//
//    /**
//     * The {@link BaseTaskQueueTier} that the last non-null return value of {@link #pollTask} was polled from,
//     * or null if {@link #pollTask} has never been called yet.
//     */
//    @ThisThreadOnly
//    private @Nullable BaseTaskQueueTier lastPolledTaskTier;
//
//    /**
//     * The lock to guard this thread's sleeping and waking actions.
//     */
//    private final Lock waitLock = new ReentrantLock();
//
//    /**
//     * The condition to wait for a signal, when this thread has to wait for something to do.
//     */
//    private final Condition waitCondition = waitLock.newCondition();
//
//    /**
//     * Whether this thread is currently not working on the content of a task, but instead
//     * attempting to poll a next task to do, checking whether it can accept tasks at all, or
//     * attempting to acquire a {@link YieldingLock}, or waiting (although the fact that this value is true during
//     * waiting is irrelevant, because at such a time, {@link #isWaiting} will be true, and this value will no longer
//     * have any effect due to the implementation of {@link #signal}).
//     * <br>
//     * This value is used to determine whether to set {@link #skipNextWait} when {@link #signal} is called
//     * and {@link #isWaiting} is false.
//     */
//    @AnyThreadSafe(Access.READ) @ThisThreadOnly(Access.WRITE)
//    private volatile boolean isPollingTaskOrCheckingStopCondition = true;
//
//    /**
//     * Whether this thread should not start waiting for something to do the next time no task could be polled,
//     * but instead try polling a task again.
//     */
//    @AnyThreadSafe
//    public volatile boolean skipNextWait = false;
//
//    /**
//     * Whether this thread is currently waiting for something to do.
//     * <br>
//     * This is set to true at some point before actually starting to wait in a blocking fashion,
//     * and set to false at some point after no longer waiting in a blocking fashion. So, at some point,
//     * this value may be true while the thread is not blocked yet, or anymore.
//     * Even more so, extra checks for whether the thread should block will be performed in between
//     * the moment this value is set to true and the moment the thread potentially blocks. This means that if the
//     * checks fail, this value may be set to true and then false again, without actually ever blocking.
//     */
//    @AnyThreadSafe(Access.READ) @ThisThreadOnly(Access.WRITE)
//    @Guarded(value = "#waitLock", fieldAccess = Access.WRITE)
//    public volatile boolean isWaiting = false;
//
//    /**
//     * Whether {@link #isWaiting} is irrelevant because this thread has already
//     * been signalled via {@link #signal} to wake up.
//     */
//    @AnyThreadSafe(Access.READ) @ThisThreadOnly(Access.WRITE)
//    @Guarded(value = "#waitLock", fieldAccess = Access.WRITE)
//    public volatile boolean mayBeStillWaitingButHasBeenSignalled = false;
//
//    /**
//     * The {@link YieldingLock} that this thread is waiting for,
//     * or null if this thread is not waiting for a {@link YieldingLock}.
//     * This value only has meaning while {@link #isWaiting} is true.
//     */
//    @AnyThreadSafe(Access.READ) @ThisThreadOnly(Access.WRITE)
//    @Guarded(value = "#waitLock", fieldAccess = Access.WRITE)
//    public volatile @Nullable YieldingLock lockWaitingFor = null;
//
//    /**
//     * The value of {@link #lockWaitingFor} during the last wait (a call to {@link Condition#await})
//     * or pre-wait check (while {@link #isNotActuallyWaitingYet} is true).
//     */
//    @ThisThreadOnly
//    private @Nullable YieldingLock lastLockWaitedFor = null;
//
//    /**
//     * A special flag, used after changing {@link #isWaiting}, when the lock must be temporarily released to
//     * call {@link BaseThreadActivation#callForUpdate()} (to avoid deadlocks in {@link #signal} calls),
//     * and we wish the pool to regard this thread as waiting
//     * (which it will, because {@link #isWaiting} will be true), but we must still
//     * know not to signal the underlying {@link #waitCondition}, but set {@link #skipNextWait} to true,
//     * when {@link #signal} is called at some point during the short release of {@link #waitLock}.
//     */
//    public volatile boolean isNotActuallyWaitingYet = false;
//
//    /**
//     * The last reason this thread was signalled before the current poll attempt, or null if the current
//     * poll attempt was not preceded by signalling (but by yielding for example).
//     */
//    public volatile @Nullable SignalReason lastSignalReason = null;
//
//    protected BaseThread(Runnable target, String name, int baseThreadIndex, int maximumYieldDepth) {
//        super(target, name);
//        this.baseThreadIndex = baseThreadIndex;
//        this.maximumYieldDepth = maximumYieldDepth;
//    }
//
//    @Override
//    public boolean holdsYieldingLock() {
//        return this.heldYieldingLockCount > 0;
//    }
//
//    @Override
//    public void incrementHeldYieldingLockCount() {
//        this.heldYieldingLockCount++;
//        if (this.heldYieldingLockCount == 1) {
//            this.updateCanStartYieldingTasks();
//        }
//    }
//
//    @Override
//    public void decrementHeldYieldingLockCount() {
//        this.heldYieldingLockCount--;
//        if (this.heldYieldingLockCount == 0) {
//            this.updateCanStartYieldingTasks();
//        }
//    }
//
//    /**
//     * Updates {@link #canStartYieldingTasks} according to {@link #yieldDepth} and {@link #heldYieldingLockCount}.
//     */
//    private void updateCanStartYieldingTasks() {
//        this.canStartYieldingTasks = this.heldYieldingLockCount == 0 && this.yieldDepth < this.maximumYieldDepth;
//    }
//
//    /**
//     * This method is based on {@link #signal}.
//     * {@link #signal} must always return true if this method returns true;
//     * otherwise {@link BaseThreadActivation} will get stuck while choosing a thread to activate.
//     *
//     * @see #signal
//     */
//    @SuppressWarnings("RedundantIfStatement")
//    public boolean isWaitingAndNeedsSignal() {
//        if (this.isWaiting) {
//            if (this.isNotActuallyWaitingYet) {
//                if (!this.skipNextWait) {
//                    return true;
//                }
//                return false;
//            }
//            if (!this.mayBeStillWaitingButHasBeenSignalled) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Yields to tasks: polls and executes tasks while possible and the stop condition is not met.
//     * The stop condition is met if {@code stopCondition} is not null and returns true, or alternatively,
//     * if {@code stopCondition} is null, and {@code yieldingLock} is successfully acquired.
//     * When no tasks can be polled, this thread will block, waiting for either a task that can be executed by this
//     * thread to become available, or for the {@code yieldingLock}, if given, to be released.
//     * <br>
//     * Exactly one of {@code stopCondition} and {@code yieldingLock} must be non-null.
//     */
//    public final void yieldUntil(@Nullable Long timeoutTime, @Nullable BooleanSupplier stopCondition, @Nullable YieldingLock yieldingLock) {
//        int oldYieldDepth = this.yieldDepth;
//        int newYieldDepth = oldYieldDepth + 1;
//        this.yieldDepth = newYieldDepth;
//        if (newYieldDepth == maximumYieldDepth) {
//            this.updateCanStartYieldingTasks();
//        }
//        this.runTasksUntil(timeoutTime, stopCondition, yieldingLock);
//        this.yieldDepth = oldYieldDepth;
//        if (newYieldDepth == maximumYieldDepth) {
//            this.updateCanStartYieldingTasks();
//        }
//    }
//
//    /**
//     * This method will keep attempting to find a task to do, and execute it, and if none is found, start waiting
//     * until the {@code timeoutTime} is reached (which is compared to {@link System#nanoTime}),
//     * or the thread is signalled by {@link BaseThreadPool} or by a {@link YieldingLock}.
//     * The loop is broken as soon as the stop condition becomes true, or the given lock is successfully acquired.
//     * <br>
//     * The above is the same as {@link #yieldUntil}, except it may be called in situations that is not 'yielding',
//     * for instance the endless loop polling tasks performed by a n{@link AssistThread}. The difference with
//     * {@link #yieldUntil} is that this method does not increment or decrement things the yield depth of this thread.
//     * <br>
//     * Exactly one of {@code stopCondition} or {@code yieldingLock} must be non-null.
//     *
//     * @see #yieldUntil
//     */
//    @ThisThreadOnly
//    @PotentiallyYielding("may yield further if an executed task is potentially yielding")
//    public final void runTasksUntil(@Nullable Long timeoutTime, @Nullable BooleanSupplier stopCondition, @Nullable YieldingLock yieldingLock) {
//        if (TickThread.isTickThread()) MinecraftServer.THREAD_DEBUG_LOGGER.ifPresent(it -> it.info("running tasks until"));
//        this.isPollingTaskOrCheckingStopCondition = true;
//
//        /*
//        Endless loop that attempts to perform a task, and if one is found, tries to perform another again,
//        but if none is found, starts awaiting such a task to become available, or for the given yielding lock
//        to be released.
//         */
//        while (true) {
//            try {
//                if (timeoutTime != null && System.nanoTime() - timeoutTime >= 0) {
//                    break;
//                }
//                if (stopCondition != null) {
//                    if (this == MinecraftServerWrapper.serverThread) {
//                        MinecraftServer.currentManagedBlockStopConditionHasBecomeTrue = false;
//                    }
//                    if (stopCondition.getAsBoolean()) {
//                        if (this == MinecraftServerWrapper.serverThread) {
//                            MinecraftServer.currentManagedBlockStopConditionHasBecomeTrue = true;
//                        }
//                        break;
//                    }
//                } else {
//                    //noinspection ConstantConditions
//                    if (yieldingLock.tryLock()) {
//                        break;
//                    }
//                }
//            } finally {
//                // Make sure other threads can be signalled for the last waited-for lock again
//                if (this.lastLockWaitedFor != null) {
//                    this.lastLockWaitedFor.canBeSignalledFor = true;
//                    this.lastLockWaitedFor = null;
//                }
//            }
//
//            // If this is the original server thread, update isInSpareTimeAndHaveNoMoreTimeAndNotAlreadyBlocking
//            if (this == MinecraftServerWrapper.serverThread) {
//                MinecraftServer.isInSpareTimeAndHaveNoMoreTimeAndNotAlreadyBlocking = MinecraftServer.isInSpareTime && MinecraftServer.blockingCount == 0 && !MinecraftServer.SERVER.haveTime();
//            }
//
//            // Attempt to poll a task that can be started
//            Runnable task = this.pollTask();
//
//            // Run the task if found
//            if (task != null) {
//
//                // If this is the server thread, potentially set nextTimeAssumeWeMayHaveDelayedTasks to true
//                if (this == MinecraftServerWrapper.serverThread && !MinecraftServer.nextTimeAssumeWeMayHaveDelayedTasks && AbstractTaskQueue.taskQueuesHaveTasks(BaseTaskQueueTier.SERVER.taskQueues)) {
//                    MinecraftServer.nextTimeAssumeWeMayHaveDelayedTasks = true;
//                }
//
//                // Update highestTierOfTaskOnStack and the thread priority
//                var highestTierBeforeTask = this.highestTierOfTaskOnStack;
//                var threadPriorityBeforeTask = this.getPriority();
//                //noinspection DataFlowIssue
//                var newHighestTier = highestTierBeforeTask == null ? this.lastPolledTaskTier : highestTierBeforeTask.ordinal < this.lastPolledTaskTier.ordinal ? highestTierBeforeTask : this.lastPolledTaskTier;
//                //noinspection DataFlowIssue
//                var newThreadPriority = newHighestTier.threadPriority;
//                if (newHighestTier != highestTierBeforeTask) {
//                    this.highestTierOfTaskOnStack = newHighestTier;
//                    BaseThreadActivation.callForUpdate();
//                    if (threadPriorityBeforeTask != newThreadPriority) {
//                        this.setPriority(newThreadPriority);
//                    }
//                }
//
//                this.isPollingTaskOrCheckingStopCondition = false;
//                task.run();
//
//                // If this is the server thread, execute some chunk tasks
//                if (this == MinecraftServerWrapper.serverThread) {
//                    if (newHighestTier != BaseTaskQueueTier.SERVER) {
//                        newHighestTier = BaseTaskQueueTier.SERVER;
//                        this.highestTierOfTaskOnStack = newHighestTier;
//                        BaseThreadActivation.callForUpdate();
//                        if (newThreadPriority != newHighestTier.threadPriority) {
//                            newThreadPriority = newHighestTier.threadPriority;
//                            this.setPriority(newThreadPriority);
//                        }
//                    }
//                    MinecraftServer.SERVER.executeMidTickTasks(); // Paper - execute chunk tasks mid tick
//                }
//
//                // Reset highestTierOfTaskOnStack and the thread priority
//                if (newHighestTier != highestTierBeforeTask) {
//                    this.highestTierOfTaskOnStack = highestTierBeforeTask;
//                    BaseThreadActivation.callForUpdate();
//                    if (threadPriorityBeforeTask != newThreadPriority) {
//                        this.setPriority(threadPriorityBeforeTask);
//                    }
//                }
//
//                this.isPollingTaskOrCheckingStopCondition = true;
//                continue;
//
//            }
//
//            /*
//            If no task that can be started by this thread was found, wait for a task that we are allowed
//            to poll to become available (when that happens, the BaseThreadPool will signal this thread),
//            or for the given yielding lock to be released. This is the only time we should ever block inside
//            a potentially yielding procedure.
//             */
//            this.waitUntilSignalled(timeoutTime, yieldingLock);
//
//        }
//
//        this.isPollingTaskOrCheckingStopCondition = false;
//
//        /*
//        If the thread was signalled for another reason than the lock, but we acquired the lock instead,
//        another thread should be signalled for that reason.
//         */
//        SignalReason lastSignalReason = this.lastSignalReason;
//        if (lastSignalReason != null && yieldingLock != null && lastSignalReason != SignalReason.YIELDING_LOCK) {
//            BaseThreadActivation.callForUpdate();
//        }
//
//    }
//
//    /**
//     * @see #pollTask()
//     */
//    @ThisThreadOnly
//    @YieldFree
//    private @Nullable Runnable pollTaskFromTier(BaseTaskQueueTier tier, boolean tinyOnly) {
//        for (var queue : tier.taskQueues) {
//            // Check whether we can not yield to the queue, if we are yielding
//            boolean canQueueBeYieldedTo = queue.canBeYieldedTo();
//            if (!canQueueBeYieldedTo && this.yieldDepth > 0) {
//                continue;
//            }
//            Runnable task = tinyOnly ? queue.pollTiny(this) : queue.poll(this);
//            if (task != null) {
//                this.lastPolledTaskTier = tier;
//                return task;
//            }
//            /*
//            Check if the tier has run out of tasks for a span,
//            in order to update BaseThreadActivation#thereMayBeTasks.
//             */
//            for (int spanI = 0; spanI < TaskSpan.length; spanI++) {
//                TaskSpan span = TaskSpan.VALUES[spanI];
//                if (queue.canHaveTasks(span)) {
//                    int oldTasks = BaseThreadActivation.thereMayBeTasks[tier.ordinal][spanI][canQueueBeYieldedTo ? 1 : 0].get();
//                    if (oldTasks > 0) {
//                        if (!queue.hasTasks(span)) {
//                            boolean tierHasNoTasksForSpan = true;
//                            for (AbstractTaskQueue otherTierQueue : tier.taskQueues) {
//                                // We already know there are no tasks in this queue
//                                if (otherTierQueue == queue) {
//                                    continue;
//                                }
//                                if (otherTierQueue.hasTasks(span)) {
//                                    tierHasNoTasksForSpan = false;
//                                    break;
//                                }
//                            }
//                            if (tierHasNoTasksForSpan) {
//                                // Set thereMayBeTasks to false, but only if it did not change in the meantime
//                                BaseThreadActivation.thereMayBeTasks[tier.ordinal][spanI][canQueueBeYieldedTo ? 1 : 0].compareAndSet(oldTasks, 0);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Polls a task from any queue this thread can currently poll from, and returns it.
//     * Polling potentially yielding tasks is attempted before yield-free tasks.
//     *
//     * @return The task that was polled, or null if no task was found.
//     */
//    @ThisThreadOnly
//    @YieldFree
//    private @Nullable Runnable pollTask() {
//        /*
//         * If this is a server thread, poll from SERVER, and poll tiny tasks from other tiers.
//         * Note that when polling on the ServerThread, we do not check whether we would be allowed to do so
//         * by the BaseThreadPool, as we consider keeping the ServerThread in the Thread.State.RUNNABLE state for
//         * as long as possible to be more important than the off-chance of for example starting a TINY ASYNC task
//         * on the server thread while no ASYNC tasks are allowed to be polled by other threads at the moment.
//         */
//        if (this instanceof ServerThread) {
//            // Poll from the SERVER queues
//            Runnable task = this.pollTaskFromTier(BaseTaskQueueTier.SERVER, false);
//            if (task != null) {
//                return task;
//            }
//            // Poll tiny tasks from other tiers
//            for (var tier : BaseTaskQueueTier.VALUES_EXCEPT_SERVER) {
//                task = this.pollTaskFromTier(tier, true);
//                if (task != null) {
//                    return task;
//                }
//            }
//            // We failed to poll any task
//            return null;
//        }
//        // If this is not a server thread, poll from all queues except SERVER
//        for (var tier : BaseTaskQueueTier.VALUES_EXCEPT_SERVER) {
//            /*
//            Make sure that we are allowed to poll from the tier, according to the presence of an excess number of
//            threads working on tasks from that tier during the last BaseThreadActivation#update call.
//            In the case this check's result is too optimistic, and a task is started when ideally it wouldn't have been,
//            then so be it - it is not terrible. Whenever this happens, enough threads will surely be allocated
//            by the BaseThreadPool for the task tier that is more in demand anyway, so it does not matter much.
//            In the case this check's result is too pessimistic, the polling fails and this thread will start to sleep,
//            but before doing this, will make a call to BaseThreadActivation#callForUpdate that re-activated this
//            thread if necessary, so no harm is done.
//            In the case this check causes this thread to go to sleep, the call to BaseThreadActivation#callForUpdate
//            while isWaiting is true will make sure the BaseThreadPool has the ability to correctly activate a
//            different thread (that is able to start tasks of a higher tier) if needed.
//            Here, we do not even make an exception for TINY tasks, since there may already be ongoing avoidable
//            context-switching due to excess threads that we can solve by letting this thread go to sleep.
//             */
//            if (tier.ordinal < BaseThreadActivation.tierInExcessOrdinal) {
//                /*
//                Tasks of a certain tier may yield to tasks of the same or a higher
//                tier, and they may also yield to tiny tasks of a lower tier.
//                 */
//                var tierYieldingFrom = this.highestTierOfTaskOnStack;
//                Runnable task = this.pollTaskFromTier(tier, tierYieldingFrom != null && tier.ordinal > tierYieldingFrom.ordinal);
//                if (task != null) {
//                    return task;
//                }
//            }
//        }
//        // We failed to poll any task
//        return null;
//    }
//
//    /**
//     * Starts waiting on something to do.
//     *
//     * @param timeoutTime The maximum time to wait until (compared to {@link System#nanoTime}).
//     * @param yieldingLock A {@link YieldingLock} to register with, or null if this thread is not waiting for
//     *                     a yielding lock.
//     */
//    @ThisThreadOnly
//    @PotentiallyBlocking
//    private void waitUntilSignalled(@Nullable Long timeoutTime, @Nullable YieldingLock yieldingLock) {
//
//        // Remember whether we registered to wait with the lock, to unregister later
//        // Register this thread with the lock if necessary
//        boolean registeredAsWaitingWithLock = false;
//        if (yieldingLock != null) {
//            // No point in registering if we're not going to wait anyway
//            if (!this.skipNextWait) {
//                yieldingLock.incrementWaitingThreads();
//                registeredAsWaitingWithLock = true;
//            }
//        }
//
//        /*
//        Remember whether we changed anything that requires a BaseThreadPool#update call
//        (after the last call to that method).
//         */
//        boolean mustCallPoolUpdateAtEnd = false;
//
//        /*
//        If we cannot acquire the lock, we can assume this thread is being signalled,
//        so there is no reason to start waiting.
//         */
//        waitWithLock: if (this.waitLock.tryLock()) {
//            try {
//
//                // If it was set that this thread should skip the wait in the meantime, skip it
//                if (this.skipNextWait) {
//                    break waitWithLock;
//                }
//
//                // Mark this thread as waiting
//                this.lockWaitingFor = yieldingLock;
//                this.mayBeStillWaitingButHasBeenSignalled = false;
//                this.isWaiting = true;
//                // But actually we are not waiting yet, signal has no effect yet during the next short lock release
//                this.isNotActuallyWaitingYet = true;
//
//            } finally {
//                this.waitLock.unlock();
//            }
//
//            // Update the pool
//            BaseThreadActivation.callForUpdate();
//
//            /*
//            If we cannot acquire the lock, we can assume this thread is being signalled,
//            so there is no reason to start waiting.
//             */
//            if (this.waitLock.tryLock()) {
//                try {
//
//                    // We passed the short lock release
//                    this.isNotActuallyWaitingYet = false;
//
//                    // If it was set that this thread should skip the wait in the meantime, skip it
//                    if (this.skipNextWait) {
//                        this.isWaiting = false;
//                        this.lastLockWaitedFor = this.lockWaitingFor;
//                        this.lockWaitingFor = null;
//                        mustCallPoolUpdateAtEnd = true;
//                        break waitWithLock;
//                    }
//
//                    // Wait
//                    try {
//
//                        // -1 indicates to not use a timeout (this value is not later set to any other negative value)
//                        long waitForNanos = -1;
//                        if (timeoutTime != null) {
//                            waitForNanos = Math.max(timeoutTime - System.nanoTime(), SERVER_THREAD_WAIT_NANOS_MINIMUM);
//                        } else {
//                            /*
//                            Check if we should wait with a tick-based timeout:
//                            this only happens if this thread is the server thread, in
//                            which case we do not want to wait past the start of the next tick.
//                             */
//                            if (this == MinecraftServerWrapper.serverThread) {
//                                if (MinecraftServer.isWaitingUntilNextTick) {
//                                    /*
//                                    During waiting until the next tick, we wait until the next tick start.
//                                    If it already passed, we do not have to use a timeout, because we will be notified
//                                    when the stop condition becomes true.
//                                     */
//                                    waitForNanos = MinecraftServer.nextTickStartNanoTime - System.nanoTime();
//                                    if (waitForNanos < 0) {
//                                        waitForNanos = -1;
//                                    }
//                                } else if (MinecraftServer.SERVER.isOversleep) {
//                                    /*
//                                    During this phase, MinecraftServer#mayHaveDelayedTasks() is checked, and we may not
//                                    be notified when it changes. Therefore, if the next tick start has not passed, we will
//                                    wait until then, but if it has, we wait for a short interval to make sure we keep
//                                    checking the stop condition (but not for longer than until the last time we can be
//                                    executing extra delayed tasks).
//                                     */
//                                    waitForNanos = MinecraftServer.nextTickStartNanoTime - System.nanoTime();
//                                    if (waitForNanos < 0) {
//                                        waitForNanos = Math.min(Math.max(0, MinecraftServer.delayedTasksMaxNextTickNanoTime - System.nanoTime()), SERVER_THREAD_WAIT_NANOS_DURING_OVERSLEEP_WITH_DELAYED_TASKS);
//                                    }
//                                }
//                            }
//                        }
//                        if (waitForNanos >= 0) {
//                            // Set the last signal reason to null in case the timeout elapses without a signal
//                            this.lastSignalReason = null;
//                            // Skip if the time is too short
//                            if (waitForNanos >= SERVER_THREAD_WAIT_NANOS_MINIMUM) {
//                                //noinspection ResultOfMethodCallIgnored
//                                this.waitCondition.await(waitForNanos, TimeUnit.NANOSECONDS);
//                            }
//                        } else {
//                            /*
//                            If we did not wait with a timeout, wait indefinitely. If this thread is the server thread,
//                            and the intended start time of the next tick has already passed, but the stop condition to stop
//                            running tasks is still not true, this thread must be signalled when a change in conditions causes
//                            the stop condition to become true.
//                             */
//                            this.waitCondition.await();
//                        }
//
//                    } catch (InterruptedException e) {
//                        throw new IllegalStateException(e);
//                    }
//
//                    // Unmark this thread as waiting
//                    this.isWaiting = false;
//                    this.lastLockWaitedFor = this.lockWaitingFor;
//                    this.lockWaitingFor = null;
//                    mustCallPoolUpdateAtEnd = true;
//
//                } finally {
//                    this.waitLock.unlock();
//                }
//            }
//
//        }
//
//        // Unregister this thread from the lock if necessary
//        if (registeredAsWaitingWithLock) {
//            yieldingLock.decrementWaitingThreads();
//        }
//
//        // Reset skipping the next wait
//        this.skipNextWait = false;
//
//        // Update the pool if necessary
//        if (mustCallPoolUpdateAtEnd) {
//            BaseThreadActivation.callForUpdate();
//        }
//
//    }
//
//    /**
//     * An auxiliary method for exclusive use in {@link #signal}, that marks the {@link YieldingLock}
//     * that this thread is waiting for as having been signalled for, so that no other threads
//     * are also signalled for it.
//     * <br>
//     * This must be called when {@link #signal} returns true, and must be called before any other
//     * actions relating to the signalling of this thread are performed.
//     */
//    private void markLockWaitingForAsSignalledFor() {
//        @Nullable YieldingLock lockWaitingFor = this.lockWaitingFor;
//        if (lockWaitingFor != null) {
//            lockWaitingFor.canBeSignalledFor = false;
//        }
//    }
//
//    /**
//     * Signals this thread to wake up, or if it was not sleeping but attempting to poll a task:
//     * to not go to sleep the next time no task could be polled, and instead try polling a task again.
//     *
//     * @param reason The reason why this thread was signalled, or null if it is irrelevant (e.g. when the signal
//     *               will never need to be repeated because there is only thread waiting for this specific event
//     *               to happen).
//     * @return Whether this thread was sleeping before, and has woken up now,
//     * or whether {@link #skipNextWait} was set to true.
//     */
//    @AnyThreadSafe
//    @YieldFree
//    public final boolean signal(@Nullable SignalReason reason) {
//        while (!this.waitLock.tryLock()) { // TODO Gale use a wait-free system here by using a sort of leave-a-message-at-the-door Atomic class system
//            Thread.onSpinWait();
//        }
//        try {
//            if (this.isWaiting) {
//                if (this.isNotActuallyWaitingYet) {
//                    if (!this.skipNextWait) {
//                        this.markLockWaitingForAsSignalledFor();
//                        this.lastSignalReason = reason;
//                        this.skipNextWait = true;
//                        return true;
//                    }
//                    return false;
//                }
//                if (!this.mayBeStillWaitingButHasBeenSignalled) {
//                    this.markLockWaitingForAsSignalledFor();
//                    this.lastSignalReason = reason;
//                    this.mayBeStillWaitingButHasBeenSignalled = true;
//                    this.waitCondition.signal();
//                    return true;
//                }
//            } else if (this.isPollingTaskOrCheckingStopCondition) {
//                if (!this.skipNextWait) {
//                    this.markLockWaitingForAsSignalledFor();
//                    this.lastSignalReason = reason;
//                    this.skipNextWait = true;
//                    return true;
//                }
//            }
//            return false;
//        } finally {
//            this.waitLock.unlock();
//        }
//    }
//
//    /**
//     * @return The current thread if it is a {@link BaseThread}, or null otherwise.
//     */
//    @SuppressWarnings("unused")
//    @AnyThreadSafe
//    @YieldFree
//    public static @Nullable BaseThread currentBaseThread() {
//        return Thread.currentThread() instanceof BaseThread baseThread ? baseThread : null;
//    }
//
//    /**
//     * @return Whether the current thread is a {@link BaseThread}.
//     */
//    @SuppressWarnings("unused")
//    @AnyThreadSafe
//    @YieldFree
//    public static boolean isBaseThread() {
//        return Thread.currentThread() instanceof BaseThread;
//    }
//
//}
