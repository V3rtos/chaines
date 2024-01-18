package me.moonways.bridgenet.mtp;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Реализации данного интерфейса означают,
 * что они содержат внутри себя канал соединения
 * внутреннего протокола MTP, и способы к
 * транспортировке сообщений.
 */
public interface MTPMessageSender {

    /**
     * Отправить сообщение на подключенный канал.
     * @param message - отправляемое сообщение.
     */
    void sendMessage(@NotNull Object message);

    /**
     * Отправить сообщение на подключенный канал
     * с ожиданием ответа.
     *
     * @param responseType - тип ожидаемого ответа.
     * @param message - отправляемое сообщение.
     */
    <R> CompletableFuture<R> sendMessageWithResponse(@NotNull Class<R> responseType, @NotNull Object message);

    /**
     * Отправить сообщение на подключенный канал
     * с ожиданием ответа.
     *
     * @param timeout - таймаут ожидания сообщения
     * @param responseType - тип ожидаемого ответа.
     * @param message - отправляемое сообщение.
     */
    <R> CompletableFuture<R> sendMessageWithResponse(int timeout, @NotNull Class<R> responseType, @NotNull Object message);
}
