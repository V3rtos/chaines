package me.moonways.bridgenet.api.util;

/**
 * Интерфейс создан по подобию функционального
 * интерфейса java Function с возможностью обработки
 * выброшенных исключений в ходе применения функции.
 *
 * @param <T> - Входящий тип объекта
 * @param <R> - Возвращаемый тип объекта.
 */
@FunctionalInterface
public interface ExceptionallyFunction<T, R> {

    R apply(T value) throws Throwable;
}
