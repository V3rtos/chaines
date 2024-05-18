package me.moonways.bridgenet.api.util;

import java.util.Objects;

/**
 * Интерфейс создан по подобию функционального
 * интерфейса java Consummer с возможностью обработки
 * выброшенных исключений в ходе применения функции.
 */
@FunctionalInterface
public interface ExceptionallyConsumer<T> {

    void accept(T obj) throws Throwable;

    default ExceptionallyConsumer<T> andThen(ExceptionallyConsumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }
}
