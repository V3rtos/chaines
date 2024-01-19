package me.moonways.bridgenet.mtp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Реализации данного интерфейса означают,
 * что они содержат внутри себя канал соединения
 * внутреннего протокола MTP, и способы к
 * транспортировке сообщений.
 */
public interface MTPMessageSender extends Serializable {

    /**
     * Получить кешированное значение аттрибута.
     * @param key - ключ аттрибута.
     */
    Optional<Object> getProperty(@NotNull String key);

    /**
     * Получить кешированное значение аттрибута
     * в виде строки.
     *
     * @param key - ключ аттрибута.
     */
    Optional<String> getPropertyString(@NotNull String key);

    /**
     * Получить кешированное значение аттрибута
     * в виде целочисленного числа.
     *
     * @param key - ключ аттрибута.
     */
    Optional<Integer> getPropertyInt(@NotNull String key);

    /**
     * Установить значение аттрибута.
     *
     * @param key - ключ аттрибута.
     * @param value - значение аттрибута.
     */
    void setProperty(@NotNull String key, @Nullable Object value);

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
