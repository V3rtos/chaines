package me.moonways.bridgenet.rest4j.data;

import lombok.experimental.UtilityClass;
import me.moonways.bridgenet.rest.model.ResponseCode;
import me.moonways.bridgenet.rest4j.Api;
import me.moonways.bridgenet.rest4j.Error;

/**
 * Утилитный класс для создания стандартных ошибок API.
 * <p>
 * Этот класс предоставляет методы для создания объектов {@link Error}
 * с предопределенными сообщениями об ошибках, соответствующими различным
 * ситуациям, таким как неправильный путь запроса, отсутствие атрибутов,
 * не найденные ресурсы и ошибки аутентификации.
 * </p>
 *
 * <p>
 * Примеры использования:
 * </p>
 * <pre>{@code
 * // Создание ошибки для неправильного пути запроса
 * Error error1 = ApiErrors.badRequestPath("/invalid-path");
 *
 * // Создание ошибки для отсутствующего атрибута
 * Error error2 = ApiErrors.noAttribute("missingAttribute");
 *
 * // Создание ошибки для не найденного ресурса
 * Error error3 = ApiErrors.notFound("User", 12345);
 * }</pre>
 */
@UtilityClass
public class ApiErrors {

    /**
     * Создает ошибку для неправильного пути запроса.
     *
     * @param path путь, который не обрабатывается API
     * @return объект {@link Error} с кодом ответа {@link ResponseCode#BAD_REQUEST}
     *         и сообщением, указывающим, что путь не обрабатывается API
     */
    public Error badRequestPath(String path) {
        return new ErrorData(ResponseCode.BAD_REQUEST, String.format("invalid path for api %s: %s", Api.VERSION_1, path));
    }

    /**
     * Создает ошибку для отсутствующего атрибута.
     *
     * @param attr имя отсутствующего атрибута
     * @return объект {@link Error} с кодом ответа {@link ResponseCode#BAD_REQUEST}
     *         и сообщением, указывающим, что атрибут не найден
     */
    public Error noAttribute(String attr) {
        return new ErrorData(ResponseCode.BAD_REQUEST, String.format("attribute `?%s` is invalid", attr));
    }

    /**
     * Создает ошибку для не найденного ресурса.
     *
     * @param type тип ресурса (например, "User")
     * @param id идентификатор ресурса
     * @return объект {@link Error} с кодом ответа {@link ResponseCode#NOT_FOUND}
     *         и сообщением, указывающим, что ресурс не найден
     */
    public Error notFound(String type, Object id) {
        return new ErrorData(ResponseCode.NOT_FOUND, String.format("%s \"%s\" not found", type, id));
    }

    /**
     * Создает ошибку для ошибки аутентификации.
     *
     * @return объект {@link Error} с кодом ответа {@link ResponseCode#FORBIDDEN}
     *         и сообщением, указывающим, что учетные данные аутентификации неправильны
     */
    public Error forbidden() {
        return new ErrorData(ResponseCode.FORBIDDEN, "auth credentials is invalid: `api-key`");
    }
}
