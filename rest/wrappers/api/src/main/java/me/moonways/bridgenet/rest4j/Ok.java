package me.moonways.bridgenet.rest4j;

/**
 * Маркерный интерфейс для обозначения успешного ответа в HTTP-запросах.
 * <p>
 * Интерфейс {@code Ok} используется как типовой параметр для обозначения успешных ответов в различных HTTP-запросах.
 * Классы, реализующие этот интерфейс, могут содержать данные, связанные с успешным выполнением запроса.
 * </p>
 *
 * <p>Пример использования:</p>
 * <pre>{@code
 * public class MyOkResponse implements Ok {
 *     private final String message;
 *     private final int code;
 *
 *     public MyOkResponse(String message, int code) {
 *         this.message = message;
 *         this.code = code;
 *     }
 *
 *     public String getMessage() {
 *         return message;
 *     }
 *
 *     public int getCode() {
 *         return code;
 *     }
 * }
 *
 * // Использование
 * Ok okResponse = new MyOkResponse("Success", 200);
 * if (okResponse instanceof MyOkResponse) {
 *     MyOkResponse response = (MyOkResponse) okResponse;
 *     System.out.println("Message: " + response.getMessage());
 *     System.out.println("Code: " + response.getCode());
 * }
 * }</pre>
 */
public interface Ok {
}
