package me.moonways.bridgenet.api.event.cansellation;

public interface Cancellable {

    boolean isCancelled();

    void makeCancelled();

    void makeNotCancelled();
}
