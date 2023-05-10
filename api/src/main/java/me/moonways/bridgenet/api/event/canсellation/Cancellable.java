package me.moonways.bridgenet.api.event.canсellation;

public interface Cancellable {

    boolean isCancelled();

    void makeCancelled();

    void makeNotCancelled();
}
