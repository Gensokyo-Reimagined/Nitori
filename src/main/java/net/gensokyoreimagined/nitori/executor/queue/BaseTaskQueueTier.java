package net.gensokyoreimagined.nitori.executor.queue;

///**
// * A tier for {@link AbstractTaskQueue}s, that indicates the priority of the tasks in the task queues.
// * Every tier contains a list of the queues that are part of the tier.
// * The tiers are in order of priority, from high to low.
// * Similarly, the queues for each tier are in the same order of priority.
// * The tasks in each queue should also be in order of priority whenever relevant, but usually there
// * is no strong difference in priority between tasks in the same queue, so they typically operate as FIFO queues,
// * so that the longest waiting task implicitly has the highest priority within the queue.
// * <br>
// * Tasks from queues in the {@link #SERVER} tier can only be run on a {@link ServerThread}.
// * Tasks from other tiers can be run on {@link ServerThread}s as well as on {@link AssistThread}s.
// *
// * @author Martijn Muijsers under AGPL-3.0
// */
//public enum BaseTaskQueueTier {
//
//    /**
//     * A tier for queues that contain tasks that must be executed on a {@link ServerThread}.
//     * <br>
//     * Some parts of the server can only be safely accessed by one thread at a time.
//     * If they can not be guarded by a lock (or if this is not desired,
//     * because if a ticking thread would need to acquire this lock it would block it),
//     * then these parts of the code are typically deferred to the server thread.
//     * Based on the current use of the {@link TickThread} class, particularly given the existence of
//     * {@link TickThread#isTickThreadFor(Entity)} and {@link TickThread#isTickThreadFor(ServerLevel, int, int)},
//     * we can deduce that future support for performing some of these actions in parallel is planned.
//     * In such a case, some server thread tasks may become tasks that must be
//     * executed on an appropriate {@link TickThread}.
//     * In that case, the queues below should be changed so that the server thread and any of the
//     * ticking threads poll from queues that contain tasks appropriate for them.
//     * For example, {@link BaseTaskQueues#deferredToUniversalTickThread} would be for tasks that can run
//     * on any ticking thread, and additional queues would need to be added concerning a specific
//     * subject (like an entity or chunk) with tasks that will be run on whichever ticking thread is the
//     * ticking thread for that subject at the time of polling.
//     * <br>
//     * Note that a {@link ServerThread} can only yield to {@link TaskSpan#TINY} tasks in other tiers
//     * (since there are no higher tiers, and threads can only yield to lower tiers when
//     * the task yielded to is{@link TaskSpan#TINY}, or other non-yielding tasks in its own tier (since it
//     * has a {@link BaseThread#maximumYieldDepth} of 1).
//     * Yielding to other tasks in this same tier is somewhat risky, since this means that the tasks that were
//     * yielded to must assume that although they are running on the server thread, they may be running at
//     * some unknown point in execution of the main thread. Therefore, scheduling any non-yielding tasks to
//     * a queue in this tier must be done with the utmost care that the task cannot disrupt, or be disrupted by,
//     * the surrounding code that yields to it.
//     */
//    SERVER(new AbstractTaskQueue[]{
//        BaseTaskQueues.deferredToServerThread,
//        BaseTaskQueues.serverThreadTick,
//        BaseTaskQueues.anyTickScheduledServerThread
//    }, MinecraftServer.SERVER_THREAD_PRIORITY),
//    /**
//     * A tier for queues that contain tasks that are part of ticking,
//     * to assist the main ticking thread(s) in doing so.
//     */
//    TICK_ASSIST(new AbstractTaskQueue[]{
//        BaseTaskQueues.tickAssist
//    }, Integer.getInteger("gale.thread.priority.tick", 7)),
//    /**
//     * A tier for queues that contain general tasks that must be performed at some point in time,
//     * asynchronously with respect to the {@link ServerThread} and the ticking of the server.
//     * Execution of
//     */
//    ASYNC(new AbstractTaskQueue[0], Integer.getInteger("gale.thread.priority.async", 6)),
//    /**
//     * A tier for queues that contain tasks with the same considerations as {@link #ASYNC},
//     * but with a low priority.
//     */
//    LOW_PRIORITY_ASYNC(new AbstractTaskQueue[0], Integer.getInteger("gale.thread.priority.async.low", 3));
//
//    /**
//     * Equal to {@link #ordinal()}.
//     */
//    public final int ordinal;
//
//    /**
//     * The task queues that belong to this tier.
//     */
//    public final AbstractTaskQueue[] taskQueues;
//
//    /**
//     * The priority for threads that are executing a task from this tier.
//     * <br>
//     * If a thread yields to other tasks, the priority it will have is always the highest priority of any task
//     * on its stack.
//     */
//    public final int threadPriority;
//
//    BaseTaskQueueTier(AbstractTaskQueue[] taskQueues, int threadPriority) {
//        this.ordinal = this.ordinal();
//        this.taskQueues = taskQueues;
//        for (AbstractTaskQueue queue : this.taskQueues) {
//            queue.setTier(this);
//        }
//        this.threadPriority = threadPriority;
//    }
//
//    /**
//     * Equal to {@link #values()}.
//     */
//    public static final BaseTaskQueueTier[] VALUES = values();
//
//    /**
//     * Equal to {@link #VALUES}{@code .length}.
//     */
//    public static final int length = VALUES.length;
//
//    /**
//     * Equal to {@link #VALUES} without {@link #SERVER}.
//     */
//    public static final BaseTaskQueueTier[] VALUES_EXCEPT_SERVER = Arrays.stream(VALUES).filter(tier -> tier != SERVER).toList().toArray(new BaseTaskQueueTier[length - 1]);
//
//}
