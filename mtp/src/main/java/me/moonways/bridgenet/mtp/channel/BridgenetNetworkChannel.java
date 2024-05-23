package me.moonways.bridgenet.mtp.channel;

import me.moonways.bridgenet.mtp.message.InboundMessageContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Реализации данного интерфейса означают,
 * что они содержат внутри себя канал соединения
 * внутреннего протокола MTP, и способы к
 * транспортировке сообщений.
 */
public interface BridgenetNetworkChannel extends Serializable {

    /**
     * Получить адрес канала.
     */
    InetSocketAddress address();

    /**
     * Получить кешированное значение аттрибута.
     *
     * @param key - ключ аттрибута.
     */
    <T> Optional<T> getProperty(@NotNull String key);

    /**
     * Установить значение аттрибута.
     *
     * @param key   - ключ аттрибута.
     * @param value - значение аттрибута.
     */
    void setProperty(@NotNull String key, @Nullable Object value);

    /**
     * Отправить сообщение на подключенный канал.
     *
     * @param message - отправляемое сообщение.
     */
    void send(@NotNull Object message);

    /**
     * Пробросить сообщение внутри сети.
     *
     * @param message - отправляемое сообщение.
     */
    void pull(@NotNull Object message);

    /**
     * Пробросить сообщение внутри сети.
     *
     * @param context - контекст отправляемого сообщения.
     */
    void pull(@NotNull InboundMessageContext<?> context);

    /**
     * Закрыть соединение с сервером.
     */
    void close();

    /**
     * Отправить сообщение на подключенный канал
     * с ожиданием ответа.
     *
     * @param responseType - тип ожидаемого ответа.
     * @param message      - отправляемое сообщение.
     */
    <R> CompletableFuture<R> sendAwait(@NotNull Class<R> responseType, @NotNull Object message);

    /**
     * Отправить сообщение на подключенный канал
     * с ожиданием ответа.
     *
     * @param timeout      - таймаут ожидания сообщения
     * @param responseType - тип ожидаемого ответа.
     * @param message      - отправляемое сообщение.
     */
    <R> CompletableFuture<R> sendAwait(int timeout, @NotNull Class<R> responseType, @NotNull Object message);
}
