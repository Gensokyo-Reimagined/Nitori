package net.gensokyoreimagined.nitori.executor.thread;

/**
 * A thread created by the {@link BaseThreadPool}.
 *
 * @author Martijn Muijsers under AGPL-3.0
 */
public class AssistThread extends BaseThread {

    /**
     * The maximum yield depth. While an {@link AssistThread} has a yield depth equal to or greater than this value,
     * it can not start more potentially yielding tasks.
     */
    public static final int MAXIMUM_YIELD_DEPTH = Integer.getInteger("gale.yield.depth.max", 100);

    /**
     * The index of this thread, as needed as an argument to
     * {@link BaseThreadPool#getThreadByAssistIndex(int)}.
     */
    public final int assistThreadIndex;

    /**
     * Must only be called from {@link BaseThreadPool#addAssistThread}.
     */
    public AssistThread(int assistThreadIndex) {
        super(AssistThread::getCurrentAssistThreadAndRunForever, "Assist Thread " + assistThreadIndex, assistThreadIndex + 1, MAXIMUM_YIELD_DEPTH);
        this.assistThreadIndex = assistThreadIndex;
    }

    /**
     * Causes this thread to loop forever, always attempting to find a task to do, and if none is found,
     * registering itself with the places where a relevant task may be added in order to be signalled when
     * one is actually added.
     */
    @ThisThreadOnly
    protected void runForever() {
        this.runTasksUntil(null, () -> false, null);
    }

    /**
     * @return The current thread if it is a {@link AssistThread}, or null otherwise.
     */
    @SuppressWarnings("unused")
    @AnyThreadSafe
    @YieldFree
    public static @Nullable AssistThread currentAssistThread() {
        return Thread.currentThread() instanceof AssistThread assistThread ? assistThread : null;
    }

    /**
     * @return Whether the current thread is a {@link AssistThread}.
     */
    @SuppressWarnings("unused")
    @AnyThreadSafe
    @YieldFree
    public static boolean isAssistThread() {
        return Thread.currentThread() instanceof AssistThread;
    }

    /**
     * A method that simply acquires the {@link AssistThread} that is the current thread, and calls
     * {@link #runForever()} on it.
     */
    @BaseThreadOnly
    protected static void getCurrentAssistThreadAndRunForever() {
        ((AssistThread) Thread.currentThread()).runForever();
    }

}
