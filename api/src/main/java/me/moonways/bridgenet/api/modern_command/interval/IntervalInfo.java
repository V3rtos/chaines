package me.moonways.bridgenet.api.modern_command.interval;

import java.util.concurrent.TimeUnit;

/**
 * Данный интерфейс предоставляет данные о таймауте пользователя.
 */
public interface IntervalInfo {

    /**
     * Получить имя пользователя.
     */
    String getUserName();

    /**
     * Получить число времени.
     */
    long getNumber();

    /**
     * Получить тип времени.
     */
    TimeUnit getTime();
}
