package net.gensokyoreimagined.nitori.common.block;

public abstract class ListeningBlockStatePredicate extends TrackedBlockStatePredicate {
    public static int LISTENING_MASK;

    protected ListeningBlockStatePredicate(int index) {
        super(index);
        LISTENING_MASK |= (1 << this.getIndex());
    }
}