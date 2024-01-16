package me.moonways.bridgenet.api.event.cancellation;

/**
 * События, которые наследуют данный интерфейс,
 * могут отменять обрабатываемое событие в корне.
 */
public interface Cancellable {

    /**
     * Отменено ли событие на данный момент
     */
    boolean isCancelled();

    /**
     * Перевести статус события в отмененный.
     */
    void makeCancelled();

    /**
     * Перевести статус события в не отмененный.
     */
    void makeNotCancelled();
}
