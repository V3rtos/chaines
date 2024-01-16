package me.moonways.bridgenet.api.util;

/**
 * Интерфейс создан по подобию функционального
 * интерфейса java Runnable с возможностью обработки
 * выброшенных исключений в ходе применения функции.
 */
@FunctionalInterface
public interface ExceptionallyRunnable {

    void run() throws Throwable;
}
