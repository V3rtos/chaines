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

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<O extends Ok> implements Response<O>, Serializable {
    private static final long serialVersionUID = -5699991743988528367L;

    public static <O extends Ok> ApiResponse<O> parse(HttpResponse response, Class<O> okClass) {
        Content content = response.getContent();
        ResponseCode responseCode = response.getCode();

        if (!responseCode.isError()) {
            return new ApiResponse<>(content.asEntity(okClass), null);
        }
        if (responseCode.getCode() == ResponseCode.FORBIDDEN.getCode()) {
            return new ApiResponse<>(null, ApiErrors.forbidden());
        }
        return new ApiResponse<>(null,
                content.asEntity(ErrorData.class));
    }

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
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<Error> optionalError() {
        return Optional.empty();
    }

    @Override
    public ApiResponse<O> subscribeOk(Consumer<O> okConsumer) {
        if (okConsumer != null) {
            optionalOk().ifPresent(okConsumer);
        }
        return this;
    }

    @Override
    public ApiResponse<O> subscribeError(Consumer<Error> errorConsumer) {
        if (errorConsumer != null) {
            optionalError().ifPresent(errorConsumer);
        }
        return this;
    }

    @Override
    public ApiResponse<O> subscribe(Consumer<O> okConsumer, Consumer<Error> errorConsumer) {
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
