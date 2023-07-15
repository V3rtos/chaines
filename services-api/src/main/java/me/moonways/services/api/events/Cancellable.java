package me.moonways.services.api.events;

public interface Cancellable {

    boolean isCancelled();

    void makeCancelled();

    void makeNotCancelled();
}
