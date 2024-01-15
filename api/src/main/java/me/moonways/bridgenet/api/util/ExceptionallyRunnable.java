package me.moonways.bridgenet.api.util;

@FunctionalInterface
public interface ExceptionallyRunnable {

    void run() throws Throwable;
}
