package me.moonways.bridgenet.rest4j;

import me.moonways.bridgenet.rest.model.HttpResponse;
import me.moonways.bridgenet.rest.model.ResponseCode;

/**
 * Интерфейс для представления ошибки в HTTP-ответах.
 * <p>
 * Этот интерфейс предоставляет методы для получения кода ошибки, сообщения об ошибке и преобразования ошибки
 * в {@link HttpResponse}.
 * </p>
 *
 * <p>Пример использования:</p>
 * <pre>{@code
 * public class MyError implements Error {
 *     private final ResponseCode code;
 *     private final String message;
 *     private final HttpResponse response;
 *
 *     public MyError(ResponseCode code, String message, HttpResponse response) {
 *         this.code = code;
 *         this.message = message;
 *         this.response = response;
 *     }
 *
 *     @Override
 *     public ResponseCode getCode() {
 *         return code;
 *     }
 *
 *     @Override
 *     public String getMessage() {
 *         return message;
 *     }
 *
 *     @Override
 *     public HttpResponse getAsResponse() {
 *         return response;
 *     }
 * }
 *
 * // Использование
 * Error error = new MyError(ResponseCode.NOT_FOUND, "Resource not found", someHttpResponse);
 * System.out.println("Error code: " + error.getCode());
 * System.out.println("Error message: " + error.getMessage());
 * HttpResponse response = error.getAsResponse();
 * }</pre>
 */
public interface Error {

    /**
     * Возвращает код ошибки.
     *
     * @return Код ошибки в виде {@link ResponseCode}.
     */
    ResponseCode getCode();

    /**
     * Возвращает сообщение об ошибке.
     *
     * @return Сообщение об ошибке.
     */
    String getMessage();

    /**
     * Преобразует ошибку в HTTP-ответ.
     *
     * @return Объект {@link HttpResponse}, представляющий ошибку.
     */
    HttpResponse getAsResponse();
}
