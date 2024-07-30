// Taken from https://github.com/GaleMC/Gale/blob/ver/1.19.4/patches/server/0151-Base-thread-pool.patch (separate license)

package net.gensokyoreimagined.nitori.executor.thread.pool;

///**
// * A pool of threads that can perform tasks to assist the current {@link ServerThread}. These tasks can be of
// * different {@linkplain BaseTaskQueueTier tiers}.
// * <br>
// * This pool intends to keep {@link #targetParallelism} threads active at any time,
// * which includes a potentially active {@link ServerThread}.
// * <br>
// * As such, this pool is closely intertwined with the {@link ServerThread}. This pool can not control the
// * {@link ServerThread} in any way, but it is responsible for signalling the {@link ServerThread} when tasks become
// * available in a {@link BaseTaskQueueTier#SERVER} task queue, and for listening for when the {@link ServerThread}
// * becomes (in)active in order to update the number of active {@link AssistThread}s accordingly.
// * <br><br>
// * Updates to the threads in this pool are done in a lock-free manner that attempts to do the right thing with
// * the volatile information that is available. In some cases, this may cause a thread to be woken up when it
// * should not have been, and so on, but the updates being lock-free is more significant than the updates being
// * optimal in a high-contention environment. The environment is not expected to have high enough contention for
// * this to have much of an impact. Additionally, the suboptimalities in updates are always optimistic in terms of
// * making/keeping threads active rather than inactive, and can not a situation where a thread was intended
// * to be active, but ends but not being active.
// *
// * @author Martijn Muijsers under AGPL-3.0
// */
//public final class BaseThreadPool {
//
//    private BaseThreadPool() {}
//
//    public static final String targetParallelismEnvironmentVariable = "gale.threads.target";
//    public static final String maxUndisturbedLowerTierThreadCountEnvironmentVariable = "gale.threads.undisturbed";
//
//    /**
//     * The target number of threads that will be actively in use by this pool,
//     * which includes a potentially active {@link ServerThread}.
//     * <br>
//     * This value is always positive.
//     * <br>
//     * The value is currently automatically determined according to the following table:
//     * <table>
//     *     <tr><th>system threads</th><th>threads spared</th></tr>
//     *     <tr><td>&#8804; 3</td><td>0</td></tr>
//     *     <tr><td>[4, 14]</td><td>1</td></tr>
//     *     <tr><td>[15, 23]</td><td>2</td></tr>
//     *     <tr><td>[24, 37]</td><td>3</td></tr>
//     *     <tr><td>[38, 54]</td><td>4</td></tr>
//     *     <tr><td>[55, 74]</td><td>5</td></tr>
//     *     <tr><td>[75, 99]</td><td>6</td></tr>
//     *     <tr><td>[100, 127]</td><td>7</td></tr>
//     *     <tr><td>[128, 158]</td><td>8</td></tr>
//     *     <tr><td>[159, 193]</td><td>9</td></tr>
//     *     <tr><td>[194, 232]</td><td>10</td></tr>
//     *     <tr><td>[233, 274]</td><td>11</td></tr>
//     *     <tr><td>&#8805; 275</td><td>12</td></tr>
//     * </table>
//     * Then <code>target parallelism = system threads - threads spared</code>.
//     * <br>
//     * The computed value above can be overridden using the {@link #targetParallelismEnvironmentVariable}.
//     */
//    public static final int targetParallelism;
//    static {
//        int parallelismByEnvironmentVariable = Integer.getInteger(targetParallelismEnvironmentVariable, -1);
//        int targetParallelismBeforeSetAtLeastOne;
//        if (parallelismByEnvironmentVariable >= 0) {
//            targetParallelismBeforeSetAtLeastOne = parallelismByEnvironmentVariable;
//        } else {
//            int systemThreads = Runtime.getRuntime().availableProcessors();
//            int threadsSpared;
//            if (systemThreads <= 3) {
//                threadsSpared = 0;
//            } else if (systemThreads <= 14) {
//                threadsSpared = 1;
//            } else if (systemThreads <= 23) {
//                threadsSpared = 2;
//            } else if (systemThreads <= 37) {
//                threadsSpared = 3;
//            } else if (systemThreads <= 54) {
//                threadsSpared = 4;
//            } else if (systemThreads <= 74) {
//                threadsSpared = 5;
//            } else if (systemThreads <= 99) {
//                threadsSpared = 6;
//            } else if (systemThreads <= 127) {
//                threadsSpared = 7;
//            } else if (systemThreads <= 158) {
//                threadsSpared = 8;
//            } else if (systemThreads <= 193) {
//                threadsSpared = 9;
//            } else if (systemThreads <= 232) {
//                threadsSpared = 10;
//            } else if (systemThreads <= 274) {
//                threadsSpared = 11;
//            } else {
//                threadsSpared = 12;
//            }
//            targetParallelismBeforeSetAtLeastOne = systemThreads - threadsSpared;
//        }
//        targetParallelism = Math.max(1, targetParallelismBeforeSetAtLeastOne);
//    }
//
//    /**
//     * The maximum number of threads to be executing tasks, that only have tasks on their thread that are strictly
//     * below a certain tier, before a thread wishing to execute such tasks gets activated regardless.
//     * If this threshold of lower tier threads is not exceeded, activating a thread to execute a higher tier task
//     * will be delayed until one of the active threads finishes execution of their stack or blocks for another
//     * reason.
//     * <br>
//     * This value is always nonnegative.
//     * <br>
//     * This value is currently automatically determined according to the following rule:
//     * <ul>
//     *     <li>0, if {@link #targetParallelism} = 1</li>
//     *     <li>{@code max(1, floor(2/5 * }{@link #targetParallelism}{@code ))}</li>
//     * </ul>
//     * The computed value above can be overridden using the {@link #maxUndisturbedLowerTierThreadCountEnvironmentVariable}.
//     */
//    public static final int maxUndisturbedLowerTierThreadCount;
//    static {
//        int maxUndisturbedLowerTierThreadCountByEnvironmentVariable = Integer.getInteger(maxUndisturbedLowerTierThreadCountEnvironmentVariable, -1);
//        maxUndisturbedLowerTierThreadCount = maxUndisturbedLowerTierThreadCountByEnvironmentVariable >= 0 ? maxUndisturbedLowerTierThreadCountByEnvironmentVariable : targetParallelism == 1 ? 0 : Math.max(1, targetParallelism * 2 / 5);
//    }
//
//    /**
//     * An array of the {@link AssistThread}s in this pool, indexed by their {@link AssistThread#assistThreadIndex}.
//     * <br>
//     * This field must only ever be changed from within {@link #addAssistThread}.
//     */
//    private static volatile AssistThread[] assistThreads = new AssistThread[0];
//
//    /**
//     * An array of the {@link BaseThread}s in this pool, indexed by their {@link BaseThread#baseThreadIndex}.
//     * <br>
//     * This field must not be referenced anywhere outside {@link #addAssistThread} or {@link #getBaseThreads()}:
//     * it only holds the last computed value.
//     */
//    private static volatile @Nullable BaseThread @NotNull [] lastComputedBaseThreads = new BaseThread[1];
//
//    /**
//     * Creates a new {@link AssistThread}, adds it to this pool and starts it.
//     * <br>
//     * Must only be called from within {@link BaseThreadActivation#update()} while
//     * {@link BaseThreadActivation#updateOngoingOnThread} is not null.
//     */
//    public static void addAssistThread() {
//        int oldThreadsLength = assistThreads.length;
//        int newThreadsLength = oldThreadsLength + 1;
//        // Expand the thread array
//        AssistThread[] newAssistThreads = Arrays.copyOf(assistThreads, newThreadsLength);
//        // Create the new thread
//        AssistThread newThread = newAssistThreads[oldThreadsLength] = new AssistThread(oldThreadsLength);
//        // Save the new thread array
//        assistThreads = newAssistThreads;
//        // Update the assist threads in baseThreads
//        @SuppressWarnings("NonAtomicOperationOnVolatileField")
//        BaseThread[] newLastComputedBaseThreads = lastComputedBaseThreads = Arrays.copyOf(lastComputedBaseThreads, newThreadsLength + 1);
//        newLastComputedBaseThreads[newThreadsLength] = newThread;
//        // Start the thread
//        newThread.start();
//        MinecraftServer.THREAD_DEBUG_LOGGER.ifPresent(it -> it.info("Added assist thread " + newAssistThreads.length));
//    }
//
//    /**
//     * The {@link BaseThread}s ({@link ServerThread}s and {@link AssistThread}s) in this thread pool,
//     * specifically for the purpose of easy iteration.
//     * <br>
//     * Note that the {@link ServerThread} at index 0 may be null if {@link MinecraftServer#isConstructed} is false.
//     * <br>
//     * Must only be called from within {@link BaseThreadActivation#update()} while
//     * {@link BaseThreadActivation#updateOngoingOnThread} is not null.
//     */
//    static @Nullable BaseThread @NotNull [] getBaseThreads() {
//        // Store in a non-local volatile
//        @Nullable BaseThread @NotNull [] baseThreads = lastComputedBaseThreads;
//        // Update the server thread if necessary
//        baseThreads[0] = ServerThread.getInstanceIfConstructed();
//        // Return the value
//        return baseThreads;
//    }
//
//    /**
//     * This method must not be called with {@code index} = 0 while {@link MinecraftServer#isConstructed} is false.
//     *
//     * @return The {@link BaseThread} with the given {@link BaseThread#baseThreadIndex}.
//     * This must not be called
//     */
//    public static @NotNull BaseThread getThreadByBaseIndex(int index) {
//        if (index == 0) {
//            return ServerThread.getInstance();
//        }
//        return assistThreads[index - 1];
//    }
//
//    /**
//     * @return The same value as {@link #getThreadByBaseIndex} if {@link MinecraftServer#isConstructed} is true
//     * or if the given {@code index} is not 0,
//     * or null otherwise (i.e. if {@link MinecraftServer#isConstructed} is false and the given {@code index} is 0).
//     */
//    @SuppressWarnings("unused")
//    public static @Nullable BaseThread getThreadByBaseIndexIfConstructed(int index) {
//        return index != 0 || MinecraftServer.isConstructed ? getThreadByBaseIndex(index) : null;
//    }
//
//    public static AssistThread getThreadByAssistIndex(int index) {
//        return assistThreads[index];
//    }
//
//}