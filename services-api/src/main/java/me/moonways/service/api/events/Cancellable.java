package me.moonways.service.api.events;

public interface Cancellable {

    boolean isCancelled();

    void makeCancelled();

    void makeNotCancelled();
}
