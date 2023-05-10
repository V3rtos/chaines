package me.moonways.bridgenet.service.event.canсellation;

public interface Cancellable {

    boolean isCancelled();

    void makeCancelled();

    void makeNotCancelled();
}
