package me.moonways.bridgenet.protocol.message;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class MessageResponse<M extends Message> {

    private final CompletableFuture<M> futureMessage = new CompletableFuture<>();

    public void complete(M m) {
        futureMessage.complete(m);
    }

    public void whenReceived(@NotNull BiConsumer<M, Throwable> consumer) {
        futureMessage.whenComplete(consumer);
    }

    public void throwException(@NotNull Throwable throwable) {
        futureMessage.completeExceptionally(throwable);
    }
}
