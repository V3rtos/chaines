package me.moonways.bridgenet.jdbc.entity;

import lombok.*;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.*;

@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SingleFuture<T> {

    public static <T> SingleFuture<T> empty() {
        return new SingleFuture<>(CompletableFuture.completedFuture(null));
    }

    public static <T> SingleFuture<T> of(CompletableFuture<T> completableFuture) {
        return new SingleFuture<>(completableFuture);
    }

    public static <T> SingleFuture<T> of(T entity) {
        return new SingleFuture<>(CompletableFuture.completedFuture(entity));
    }

    public static <T> SingleFuture<T> supplyAsync(Supplier<T> entitySupplier) {
        return new SingleFuture<>(CompletableFuture.supplyAsync(entitySupplier));
    }

    public static <T> SingleFuture<T> supplyAsync(Supplier<T> entitySupplier, Executor executor) {
        return new SingleFuture<>(CompletableFuture.supplyAsync(entitySupplier, executor));
    }

    final CompletableFuture<T> future;

    public T block() {
        return future.join();
    }

    @SneakyThrows({InterruptedException.class, ExecutionException.class})
    public T freeze() {
        return future.get();
    }

    public SingleFuture<T> subscribe(Runnable runnable) {
        if (isPresent()) {
            runnable.run();
            return this;
        }
        future.thenRun(runnable);
        return this;
    }

    public SingleFuture<T> subscribe(Consumer<T> consumer) {
        if (isPresent()) {
            consumer.accept(block());
            return this;
        }
        future.thenAccept((t) -> { if (t != null) consumer.accept(t); });
        return this;
    }

    public SingleFuture<T> subscribe(BiConsumer<T, Throwable> consumer) {
        if (isPresent()) {
            consumer.accept(block(), null);
            return this;
        }
        future.whenComplete((t,e) -> { if (t != null) consumer.accept(t,e); });
        return this;
    }

    public SingleFuture<T> blockAndSubscribe(Runnable runnable) {
        block();
        runnable.run();
        return this;
    }

    public SingleFuture<T> blockAndSubscribe(Consumer<T> consumer) {
        T block = block();
        if (block != null) {
            consumer.accept(block);
        }
        return this;
    }

    public SingleFuture<T> blockAndSubscribe(BiConsumer<T, Throwable> consumer) {
        subscribe(consumer);
        block();
        return this;
    }

    public Optional<T> blockOptional() {
        try {
            return Optional.ofNullable(block());
        } catch (CompletionException exception) {
            return Optional.empty();
        }
    }

    public <R> SingleFuture<R> replace(Function<T, R> function) {
        return of(future.thenApply(t -> { if (t != null) return function.apply(t); else return null; }));
    }

    public SingleFuture<T> replace(UnaryOperator<T> unaryOperator) {
        return replace((Function<T, T>) unaryOperator);
    }

    public <R> SingleFuture<R> replaceFlat(Function<T, Optional<R>> function) {
        return replace((Function<T, R>) t -> function.apply(t).orElse(null));
    }

    public SingleFuture<T> filter(Predicate<T> predicate) {
        return toList().filter(predicate).first();
    }

    public boolean isPresent() {
        return future.isDone();
    }

    public boolean isCancelled() {
        return future.isCancelled();
    }

    public ListFuture<T> toList() {
        return ListFuture.ofFutures(Collections.singletonList(future));
    }
}
