package me.moonways.bridgenet.jdbc.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.*;

/**
 * Утилита для работы с асинхронными операциями, предоставляющая
 * удобные методы для обработки результатов.
 *
 * @param <T> Тип результата асинхронной операции.
 */
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Mono<T> {

    /**
     * Создает пустой Mono, завершающийся null значением.
     *
     * @param <T> Тип результата.
     * @return Пустой Mono.
     */
    public static <T> Mono<T> empty() {
        return new Mono<>(CompletableFuture.completedFuture(null));
    }

    /**
     * Создает Mono из существующего будущего результата.
     *
     * @param completableFuture Объект, представляющий асинхронную операцию.
     * @param <T> Тип результата.
     * @return Новый Mono.
     */
    public static <T> Mono<T> of(CompletableFuture<T> completableFuture) {
        return new Mono<>(completableFuture);
    }

    /**
     * Создает Mono, завершающийся указанным значением.
     *
     * @param entity Значение, которым должен завершиться Mono.
     * @param <T> Тип результата.
     * @return Новый Mono.
     */
    public static <T> Mono<T> of(T entity) {
        return new Mono<>(CompletableFuture.completedFuture(entity));
    }

    /**
     * Создает Mono, выполняющий указанного поставщика в асинхронном режиме.
     *
     * @param entitySupplier Поставщик, выполняющий асинхронную операцию.
     * @param <T> Тип результата.
     * @return Новый Mono.
     */
    public static <T> Mono<T> supplyAsync(Supplier<T> entitySupplier) {
        return new Mono<>(CompletableFuture.supplyAsync(entitySupplier));
    }

    /**
     * Создает Mono, выполняющий указанного поставщика в асинхронном
     * режиме с использованием заданного исполнителя.
     *
     * @param entitySupplier Поставщик, выполняющий асинхронную операцию.
     * @param executor Исполнитель для выполнения асинхронной операции.
     * @param <T> Тип результата.
     * @return Новый Mono.
     */
    public static <T> Mono<T> supplyAsync(Supplier<T> entitySupplier, Executor executor) {
        return new Mono<>(CompletableFuture.supplyAsync(entitySupplier, executor));
    }

    final CompletableFuture<T> future;
    private Supplier<T> orElse;
    private Predicate<T> filter;

    /**
     * Блокирует выполнение до завершения асинхронной операции и возвращает результат.
     *
     * @return Результат асинхронной операции.
     */
    public synchronized T block() {
        T join = future.join();
        if (filter != null && !filter.test(join)) {
            return orElse != null ? orElse.get() : null;
        }
        return join;
    }

    /**
     * Блокирует выполнение до завершения асинхронной операции и возвращает
     * результат в виде Optional.
     *
     * @return Optional с результатом асинхронной операции.
     */
    public synchronized Optional<T> blockOptional() {
        return blockOptional(null);
    }

    /**
     * Блокирует выполнение до завершения асинхронной операции, обрабатывая исключения.
     *
     * @param exceptionConsumer Обработчик исключений.
     * @return Optional с результатом асинхронной операции.
     */
    public synchronized Optional<T> blockOptional(Consumer<Throwable> exceptionConsumer) {
        try {
            return Optional.ofNullable(block());
        } catch (Throwable exception) {
            if (exceptionConsumer != null) {
                exceptionConsumer.accept(exception);
            }
            return Optional.empty();
        }
    }

    /**
     * Блокирует выполнение до завершения асинхронной операции и возвращает результат,
     * перехватывая исключения.
     *
     * @return Результат асинхронной операции.
     * @throws InterruptedException Если поток был прерван.
     * @throws ExecutionException Если возникло исключение при выполнении.
     */
    @SneakyThrows({InterruptedException.class, ExecutionException.class})
    public T freeze() {
        T get = future.get();
        if (filter != null && !filter.test(get)) {
            return orElse != null ? orElse.get() : null;
        }
        return get;
    }

    /**
     * Блокирует выполнение до завершения асинхронной операции с указанным таймаутом.
     *
     * @param timeout Таймаут ожидания.
     * @return Результат асинхронной операции.
     * @throws InterruptedException Если поток был прерван.
     * @throws ExecutionException Если возникло исключение при выполнении.
     * @throws TimeoutException Если время ожидания истекло.
     */
    @SneakyThrows({InterruptedException.class, ExecutionException.class, TimeoutException.class})
    public T freeze(Duration timeout) {
        if (timeout == null) {
            return freeze();
        }
        return future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Подписывается на выполнение асинхронной операции, выполняя указанный
     * Runnable после завершения.
     *
     * @param runnable Runnable для выполнения после завершения.
     * @return Текущий Mono.
     */
    public Mono<T> subscribe(Runnable runnable) {
        if (isPresent()) {
            runnable.run();
            return this;
        }
        future.thenRun(runnable);
        return this;
    }

    /**
     * Подписывается на выполнение асинхронной операции, принимая результат в
     * указанный Consumer после завершения.
     *
     * @param consumer Consumer для обработки результата.
     * @return Текущий Mono.
     */
    public Mono<T> subscribe(Consumer<T> consumer) {
        if (isPresent()) {
            consumer.accept(block());
            return this;
        }
        future.thenAccept((t) -> {
            if (filter != null && !filter.test(t)) {
                return;
            }
            if (t != null)
                consumer.accept(t);
            else if (orElse != null)
                orElse.get();
        });
        return this;
    }

    /**
     * Подписывается на выполнение асинхронной операции, принимая результат и исключение
     * в указанный BiConsumer после завершения.
     *
     * @param consumer BiConsumer для обработки результата и исключения.
     * @return Текущий Mono.
     */
    public Mono<T> subscribe(BiConsumer<T, Throwable> consumer) {
        if (isPresent()) {
            consumer.accept(block(), null);
            return this;
        }
        future.whenComplete((t, e) -> {
            if (filter != null && !filter.test(t)) {
                return;
            }
            if (t != null)
                consumer.accept(t, e);
            else if (orElse != null)
                orElse.get();
        });
        return this;
    }

    /**
     * Блокирует выполнение и подписывается на выполнение асинхронной операции, выполняя
     * указанный Runnable после завершения.
     *
     * @param runnable Runnable для выполнения после завершения.
     * @return Текущий Mono.
     */
    public Mono<T> blockAndSubscribe(Runnable runnable) {
        block();
        if (runnable != null) {
            runnable.run();
        }
        return this;
    }

    /**
     * Блокирует выполнение и подписывается на выполнение асинхронной операции, принимая
     * результат в указанный Consumer после завершения.
     *
     * @param consumer Consumer для обработки результата.
     * @return Текущий Mono.
     */
    public Mono<T> blockAndSubscribe(Consumer<T> consumer) {
        T t = blockOptional().orElseGet(orElse != null ? orElse : () -> null);
        if (t != null || orElse != null) {
            consumer.accept(t);
        }
        return this;
    }

    /**
     * Блокирует выполнение и подписывается на выполнение асинхронной операции, принимая
     * результат и исключение в указанный BiConsumer после завершения.
     *
     * @param consumer BiConsumer для обработки результата и исключения.
     * @return Текущий Mono.
     */
    public Mono<T> blockAndSubscribe(BiConsumer<T, Throwable> consumer) {
        subscribe(consumer);
        block();
        return this;
    }

    /**
     * Преобразует результат асинхронной операции с помощью указанной функции.
     *
     * @param function Функция для преобразования результата.
     * @param <R> Тип результата после преобразования.
     * @return Новый Mono с преобразованным результатом.
     */
    public <R> Mono<R> map(Function<T, R> function) {
        return of(future.thenApply(t -> {
            if (t != null)
                return function.apply(t);
            else if (orElse != null)
                return function.apply(orElse.get());
            else return null;
        }));
    }

    /**
     * Преобразует результат асинхронной операции с помощью указанной функции,
     * возвращающей Optional.
     *
     * @param function Функция для преобразования результата.
     * @param <R> Тип результата после преобразования.
     * @return Новый Mono с преобразованным результатом.
     */
    public <R> Mono<R> flatMap(Function<T, Optional<R>> function) {
        return map(t -> function.apply(t).orElse(null));
    }

    /**
     * Фильтрует результат асинхронной операции с помощью указанного предиката.
     *
     * @param predicate Предикат для фильтрации результата.
     * @return Новый Mono с отфильтрованным результатом.
     */
    public Mono<T> filter(Predicate<T> predicate) {
        if (filter == null) {
            filter = predicate;
        } else {
            filter = filter.and(predicate);
        }
        return this;
    }

    /**
     * Проверяет, завершилась ли асинхронная операция.
     *
     * @return true, если операция завершена, иначе false.
     */
    public boolean isPresent() {
        return future.isDone();
    }

    /**
     * Проверяет, была ли отменена асинхронная операция.
     *
     * @return true, если операция была отменена, иначе false.
     */
    public boolean isCancelled() {
        return future.isCancelled();
    }

    /**
     * Устанавливает значение, которое будет возвращено, если асинхронная операция
     * завершится null значением.
     *
     * @param def Значение по умолчанию.
     * @return Текущий Mono.
     */
    public Mono<T> orElse(T def) {
        this.orElse = () -> def;
        return this;
    }

    /**
     * Устанавливает поставщика значения, которое будет возвращено, если асинхронная операция
     * завершится null значением.
     *
     * @param def Поставщик значения по умолчанию.
     * @return Текущий Mono.
     */
    public Mono<T> orElse(Supplier<T> def) {
        this.orElse = def;
        return this;
    }

    /**
     * Устанавливает поставщика Optional значения, которое будет возвращено, если асинхронная
     * операция завершится null значением.
     *
     * @param def Поставщик Optional значения по умолчанию.
     * @return Текущий Mono.
     */
    public Mono<T> or(Supplier<Optional<T>> def) {
        this.orElse = () -> def.get().orElse(null);
        return this;
    }

    /**
     * Устанавливает Optional значение, которое будет возвращено, если асинхронная операция
     * завершится null значением.
     *
     * @param def Optional значение по умолчанию.
     * @return Текущий Mono.
     */
    public Mono<T> or(Optional<T> def) {
        this.orElse = () -> def.orElse(null);
        return this;
    }

    /**
     * Преобразует текущий Mono в Multiple, содержащий один элемент.
     *
     * @return Multiple, содержащий один элемент.
     */
    public Multiple<T> toMultiple() {
        return Multiple.ofFutures(Collections.singletonList(future));
    }
}
