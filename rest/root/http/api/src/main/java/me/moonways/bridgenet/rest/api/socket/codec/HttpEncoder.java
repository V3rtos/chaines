package me.moonways.bridgenet.rest.api.socket.codec;

import me.moonways.bridgenet.rest.model.HttpRequest;
import me.moonways.bridgenet.rest.model.HttpResponse;

import java.io.ByteArrayOutputStream;

/**
 * Интерфейс для кодировщика HTTP сообщений.
 */
public interface HttpEncoder extends StreamCodec {

    /**
     * Кодирует HTTP запрос в массив байтов.
     *
     * @param httpRequest HTTP запрос для кодирования
     * @return массив байтов, содержащий закодированный HTTP запрос
     */
    ByteArrayOutputStream encode0(HttpRequest httpRequest);

    /**
     * Кодирует HTTP ответ в массив байтов.
     *
     * @param httpResponse HTTP ответ для кодирования
     * @return массив байтов, содержащий закодированный HTTP ответ
     */
    ByteArrayOutputStream encode1(HttpResponse httpResponse);
}
