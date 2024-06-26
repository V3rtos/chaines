package me.moonways.bridgenet.rest.api;

import me.moonways.bridgenet.rest.model.HttpRequest;
import me.moonways.bridgenet.rest.model.HttpResponse;

/**
 * Интерфейс для обработчика HTTP запросов.
 */
public interface HttpListener {

    /**
     * Константа для обозначения пропуска действия.
     */
    HttpResponse SKIP_ACTION = null;

    /**
     * Метод для обработки HTTP запроса и возврата HTTP ответа.
     *
     * @param request HTTP запрос
     * @return HTTP ответ
     */
    HttpResponse process(HttpRequest request);
}
