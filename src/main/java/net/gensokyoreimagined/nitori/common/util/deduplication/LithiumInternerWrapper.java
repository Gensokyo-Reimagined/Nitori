package net.gensokyoreimagined.nitori.common.util.deduplication;

public interface LithiumInternerWrapper<T> {

    T lithium$getCanonical(T value);

    void lithium$deleteCanonical(T value);
}