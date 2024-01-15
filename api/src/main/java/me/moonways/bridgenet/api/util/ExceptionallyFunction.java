package me.moonways.bridgenet.api.util;

@FunctionalInterface
public interface ExceptionallyFunction<T, R> {

    R apply(T value) throws Throwable;
}
