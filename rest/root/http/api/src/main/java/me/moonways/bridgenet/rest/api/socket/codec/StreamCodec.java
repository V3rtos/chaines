package me.moonways.bridgenet.rest.api.socket.codec;

import me.moonways.bridgenet.rest.model.HttpProtocol;

/**
 * Интерфейс для кодека потока данных.
 */
public interface StreamCodec {

    /**
     * Возвращает протокол, с которым работает данный кодек.
     *
     * @return протокол HTTP
     */
    HttpProtocol protocol();
}
