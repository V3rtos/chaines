package me.moonways.service.event.canсellation;

public interface Cancellable {

    boolean isCancelled();

    void makeCancelled();

    void makeNotCancelled();
}
