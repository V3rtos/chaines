package me.moonways.bridgenet.rest4j;

import lombok.*;
import me.moonways.bridgenet.rest.model.Content;
import me.moonways.bridgenet.rest.model.HttpResponse;
import me.moonways.bridgenet.rest.model.ResponseCode;
import me.moonways.bridgenet.rest4j.data.ErrorData;
import me.moonways.bridgenet.rest4j.data.ApiErrors;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Класс для представления API ответа, который может быть либо успешным, либо ошибочным.
 * <p>
 * Этот класс используется для обработки HTTP-ответов от API, предоставляя методы для проверки,
 * обработки и получения данных успешного ответа или ошибки.
 * </p>
 * <p>
 * Примеры использования:
 * </p>
 * <pre>{@code
 * // Пример успешного ответа
 * HttpResponse httpResponse = ... // получить HTTP-ответ
 * ApiResponse<MyOkResponse> response = ApiResponse.parse(httpResponse, MyOkResponse.class);
 * if (response.isOk()) {
 *     MyOkResponse okResponse = response.getOk();
 *     // Обработка успешного ответа
 * } else {
 *     Error error = response.getError();
 *     // Обработка ошибки
 * }
 *
 * // Пример обработки ошибок
 * response.subscribeError(error -> {
 *     System.err.println("Ошибка: " + error.getMessage());
 * }).printError();
 * }</pre>
 *
 * @param <O> тип успешного ответа, должен реализовывать интерфейс {@link Ok}
 */
@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<O extends Ok> implements Response<O>, Serializable {
    private static final long serialVersionUID = -5699991743988528367L;

    /**
     * Создает экземпляр {@code ApiResponse} на основе HTTP-ответа.
     *
     * @param response HTTP-ответ
     * @param okClass  класс успешного ответа
     * @param <O>      тип успешного ответа, должен реализовывать интерфейс {@link Ok}
     * @return экземпляр {@code ApiResponse}
     */
    public static <O extends Ok> ApiResponse<O> parse(HttpResponse response, Class<O> okClass) {
        Content content = response.getContent();
        ResponseCode responseCode = response.getCode();

        if (!responseCode.isError()) {
            return new ApiResponse<>(content.asEntity(okClass), null);
        }
        if (responseCode.getCode() == ResponseCode.FORBIDDEN.getCode()) {
            return new ApiResponse<>(null,
                    ApiErrors.forbidden());
        }
        return new ApiResponse<>(null,
                content.asEntity(ErrorData.class));
    }

    /**
     * Создает экземпляр {@code ApiResponse} при истечении времени ожидания подключения.
     *
     * @param message сообщение об ошибке
     * @param ex      исключение
     * @param <O>     тип успешного ответа, должен реализовывать интерфейс {@link Ok}
     * @return экземпляр {@code ApiResponse}
     */
    public static <O extends Ok> ApiResponse<O> connectionTimedOut(String message, Throwable ex) {
        return new ApiResponse<>(null,
                new ErrorData(ResponseCode.CONNECTION_TIMED_OUT,
                        message + (ex != null ? ": " + ex : "")));
    }

    private final O ok;
    private final Error error;

    @Override
    public boolean isOk() {
        return optionalOk().isPresent();
    }

    @Override
    public boolean isError() {
        return optionalError().isPresent();
    }

    @Override
    public @NotNull Optional<O> optionalOk() {
        return Optional.ofNullable(ok);
    }

    @Override
    public @NotNull Optional<Error> optionalError() {
        return Optional.ofNullable(error);
    }

    @Override
    public Response<O> subscribeOk(Consumer<O> okConsumer) {
        if (okConsumer != null) {
            optionalOk().ifPresent(okConsumer);
        }
        return this;
    }

    @Override
    public Response<O> subscribeError(Consumer<Error> errorConsumer) {
        if (errorConsumer != null) {
            optionalError().ifPresent(errorConsumer);
        }
        return this;
    }

    @Override
    public Response<O> subscribe(Consumer<O> okConsumer, Consumer<Error> errorConsumer) {
        return subscribeOk(okConsumer).subscribeError(errorConsumer);
    }

    @Override
    public Response<O> printError(PrintWriter printWriter) {
        optionalError()
                .map(er -> new ApiException(er.getMessage()))
                .ifPresent(exception -> exception.printStackTrace(printWriter));
        return this;
    }

    @Override
    public Response<O> printError(PrintStream printStream) {
        return printError(new PrintWriter(printStream));
    }

    @Override
    public Response<O> printError() {
        return printError(System.out);
    }
}
