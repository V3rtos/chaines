package me.moonways.bridgenet.jdbc.entity;

import com.google.common.collect.Iterables;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ListFuture<T> {

    public static <T> ListFuture<T> empty() {
        return new ListFuture<>(Collections.synchronizedList(new ArrayList<>()));
    }

    public static <T> ListFuture<T> ofFutures(List<CompletableFuture<T>> futures) {
        return new ListFuture<>(futures);
    }

    public static <T> ListFuture<T> of(Iterable<T> entities, Class<T> cls) {
        return new ListFuture<>(Arrays.stream(Iterables.toArray(entities, cls))
                .map(CompletableFuture::completedFuture)
                .collect(Collectors.toCollection(() -> Collections.synchronizedList(new ArrayList<>()))));
    }

    public static <T> ListFuture<T> supplyAsync(List<Supplier<T>> entitySuppliers) {
        return new ListFuture<>(entitySuppliers.stream()
                .map(CompletableFuture::supplyAsync)
                .collect(Collectors.toCollection(() -> Collections.synchronizedList(new ArrayList<>()))));
    }

    public static <T> ListFuture<T> supplyAsync(List<Supplier<T>> entitySuppliers, Executor executor) {
        return new ListFuture<>(entitySuppliers.stream()
                .map(supplier -> CompletableFuture.supplyAsync(supplier, executor))
                .collect(Collectors.toCollection(() -> Collections.synchronizedList(new ArrayList<>()))));
    }

    private final List<CompletableFuture<T>> futures;

    public List<T> blockAll() {
        return futures.stream().map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public List<T> freezeAll() {
        return futures.stream().map(SingleFuture::of).map(SingleFuture::freeze)
                .collect(Collectors.toList());
    }

    public <C extends Collection<T>> C blockAll(Supplier<C> collectionSupplier) {
        return futures.stream().map(CompletableFuture::join)
                .collect(Collectors.toCollection(collectionSupplier));
    }

    public ListFuture<T> subscribeEach(Runnable runnable) {
        futures.stream().map(SingleFuture::of).forEach(future -> future.subscribe(runnable));
        return this;
    }

    public ListFuture<T> subscribeEach(Consumer<T> consumer) {
        futures.stream().map(SingleFuture::of).forEach(future -> future.subscribe(consumer));
        return this;
    }

    public ListFuture<T> subscribeEach(BiConsumer<T, Throwable> consumer) {
        futures.stream().map(SingleFuture::of).forEach(future -> future.subscribe(consumer));
        return this;
    }

    public ListFuture<T> subscribeFirst(Runnable runnable) {
        CompletableFuture<T> first = Iterables.getFirst(futures, null);
        if (first != null) {
            SingleFuture.of(first)
                    .subscribe(runnable);
        }
        return this;
    }

    public ListFuture<T> subscribeFirst(Consumer<T> consumer) {
        CompletableFuture<T> first = Iterables.getFirst(futures, null);
        if (first != null) {
            SingleFuture.of(first)
                    .subscribe(consumer);
        }
        return this;
    }

    public ListFuture<T> subscribeFirst(BiConsumer<T, Throwable> consumer) {
        CompletableFuture<T> first = Iterables.getFirst(futures, null);
        if (first != null) {
            SingleFuture.of(first)
                    .subscribe(consumer);
        }
        return this;
    }

    public ListFuture<T> subscribeLast(Runnable runnable) {
        CompletableFuture<T> last = Iterables.getLast(futures, null);
        if (last != null) {
            SingleFuture.of(last)
                    .subscribe(runnable);
        }
        return this;
    }

    public ListFuture<T> subscribeLast(Consumer<T> consumer) {
        CompletableFuture<T> last = Iterables.getLast(futures, null);
        if (last != null) {
            SingleFuture.of(last)
                    .subscribe(consumer);
        }
        return this;
    }

    public ListFuture<T> subscribeLast(BiConsumer<T, Throwable> consumer) {
        CompletableFuture<T> last = Iterables.getLast(futures, null);
        if (last != null) {
            SingleFuture.of(last)
                    .subscribe(consumer);
        }
        return this;
    }

    public ListFuture<T> blockAndSubscribe(Runnable runnable) {
        blockAll();
        runnable.run();
        return this;
    }

    public ListFuture<T> blockAndSubscribeEach(Runnable runnable) {
        blockAll().forEach(t -> runnable.run());
        return this;
    }

    public ListFuture<T> blockAndSubscribeEach(Consumer<T> consumer) {
        blockAll().forEach(consumer);
        return this;
    }

    public ListFuture<T> subscribeExceptionally(BiConsumer<T, Throwable> consumer) {
        futures.forEach(completableFuture -> completableFuture.whenComplete(consumer));
        return this;
    }

    public ListFuture<T> filter(Predicate<T> predicate) {
        new ArrayList<>(futures).forEach(completableFuture -> {
            completableFuture.thenAccept(t -> {
                if (!predicate.test(t))
                    futures.remove(completableFuture);
            });
        });
        return this;
    }

    public Optional<List<T>> blockOptional() {
        try {
            return Optional.ofNullable(blockAll());
        } catch (CompletionException exception) {
            return Optional.empty();
        }
    }

    public <R> ListFuture<R> replaceEach(Function<T, R> function) {
        return ofFutures(futures.stream().map(SingleFuture::of).map(future -> future.replace(function).future)
                .collect(Collectors.toList()));
    }

    public boolean isPresentAll() {
        return futures.stream().allMatch(CompletableFuture::isDone);
    }

    public SingleFuture<T> first() {
        return Optional.ofNullable(Iterables.getFirst(futures, null))
                .map(SingleFuture::of)
                .orElseGet(SingleFuture::empty);
    }

    public SingleFuture<T> last() {
        return Optional.ofNullable(Iterables.getLast(futures, null))
                .map(SingleFuture::of)
                .orElseGet(SingleFuture::empty);
    }

    public ListFuture<T> add(SingleFuture<T> other) {
        futures.add(other.future);
        return this;
    }

    public ListFuture<T> addAll(ListFuture<T> other) {
        futures.addAll(other.futures);
        return this;
    }

    public int size() {
        return futures.size();
    }

    public boolean isEmpty() {
        return futures.isEmpty();
    }
}
