package me.moonways.bridgenet.rest4j;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.function.Consumer;

public interface Response<O extends Ok> {

    boolean isOk();

    boolean isError();

    @Nullable
    O getOk();

    @Nullable
    Error getError();

    @NotNull
    Optional<O> optionalOk();

    @NotNull
    Optional<Error> optionalError();

    Response<O> subscribeOk(Consumer<O> okConsumer);

    Response<O> subscribeError(Consumer<Error> errorConsumer);

    Response<O> subscribe(Consumer<O> okConsumer, Consumer<Error> errorConsumer);

    Response<O> printError(PrintWriter printWriter);

    Response<O> printError(PrintStream printStream);

    Response<O> printError();
}
