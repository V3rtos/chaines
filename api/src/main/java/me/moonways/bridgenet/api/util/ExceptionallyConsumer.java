package me.moonways.bridgenet.api.util;

/**
 * Интерфейс создан по подобию функционального
 * интерфейса java Consummer с возможностью обработки
 * выброшенных исключений в ходе применения функции.
 */
@FunctionalInterface
public interface ExceptionallyConsumer<T> {

    void accept(T obj) throws Throwable;
}
