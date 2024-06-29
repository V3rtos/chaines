package me.moonways.bridgenet.rest4j;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Интерфейс, представляющий ответ от сервера, который может быть либо успешным, либо содержащим ошибку.
 *
 * <p>Пример использования:</p>
 * <pre>{@code
 * Response<MyOk> response = ApiResponse.parse(...);
 *
 * // Проверка успешного ответа
 * if (response.isOk()) {
 *     MyOk ok = response.getOk();
 *     // Обработка успешного ответа
 * } else {
 *     Error error = response.getError();
 *     // Обработка ошибки
 * }
 *
 * // Использование Optional
 * response.optionalOk().ifPresent(ok -> {
 *     // Обработка успешного ответа
 * });
 * response.optionalError().ifPresent(error -> {
 *     // Обработка ошибки
 * });
 *
 * // Подписка на ответ
 * response.subscribe(
 *     ok -> {
 *         // Обработка успешного ответа
 *     },
 *     error -> {
 *         // Обработка ошибки
 *     }
 * );
 *
 * // Печать ошибки
 * response.printError();
 * }</pre>
 */
public interface Response<O extends Ok> {

    /**
     * Проверяет, является ли ответ успешным.
     *
     * @return {@code true}, если ответ успешен, иначе {@code false}.
     */
    boolean isOk();

    /**
     * Проверяет, содержит ли ответ ошибку.
     *
     * @return {@code true}, если ответ содержит ошибку, иначе {@code false}.
     */
    boolean isError();

    /**
     * Возвращает успешный ответ, если он присутствует.
     *
     * @return Успешный ответ или {@code null}, если ответ содержит ошибку.
     */
    @Nullable
    O getOk();

    /**
     * Возвращает ошибку, если она присутствует.
     *
     * @return Ошибка или {@code null}, если ответ успешен.
     */
    @Nullable
    Error getError();

    /**
     * Возвращает успешный ответ в виде {@link Optional}.
     *
     * @return {@link Optional}, содержащий успешный ответ, если он присутствует, иначе пустой {@link Optional}.
     */
    @NotNull
    Optional<O> optionalOk();

    /**
     * Возвращает ошибку в виде {@link Optional}.
     *
     * @return {@link Optional}, содержащий ошибку, если она присутствует, иначе пустой {@link Optional}.
     */
    @NotNull
    Optional<Error> optionalError();

    /**
     * Выполняет действие при успешном ответе.
     *
     * @param okConsumer {@link Consumer}, который будет выполнен, если ответ успешен.
     * @return Текущий экземпляр {@link Response} для цепочного вызова.
     */
    Response<O> subscribeOk(Consumer<O> okConsumer);

    /**
     * Выполняет действие при ошибке в ответе.
     *
     * @param errorConsumer {@link Consumer}, который будет выполнен, если ответ содержит ошибку.
     * @return Текущий экземпляр {@link Response} для цепочного вызова.
     */
    Response<O> subscribeError(Consumer<Error> errorConsumer);

    /**
     * Выполняет действие в зависимости от типа ответа (успешный или ошибочный).
     *
     * @param okConsumer    {@link Consumer}, который будет выполнен, если ответ успешен.
     * @param errorConsumer {@link Consumer}, который будет выполнен, если ответ содержит ошибку.
     * @return Текущий экземпляр {@link Response} для цепочного вызова.
     */
    Response<O> subscribe(Consumer<O> okConsumer, Consumer<Error> errorConsumer);

    /**
     * Печатает ошибку в {@link PrintWriter}.
     *
     * @param printWriter {@link PrintWriter}, в который будет выведена ошибка.
     * @return Текущий экземпляр {@link Response} для цепочного вызова.
     */
    Response<O> printError(PrintWriter printWriter);

    /**
     * Печатает ошибку в {@link PrintStream}.
     *
     * @param printStream {@link PrintStream}, в который будет выведена ошибка.
     * @return Текущий экземпляр {@link Response} для цепочного вызова.
     */
    Response<O> printError(PrintStream printStream);

    /**
     * Печатает ошибку в стандартный поток вывода ошибок.
     *
     * @return Текущий экземпляр {@link Response} для цепочного вызова.
     */
    Response<O> printError();
}
