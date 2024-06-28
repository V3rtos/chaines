package me.moonways.bridgenet.jdbc.entity;

import lombok.*;

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
public class SingleFuture<T> {

    /**
     * Создает пустой SingleFuture, завершающийся null значением.
     *
     * @param <T> Тип результата.
     * @return Пустой SingleFuture.
     */
    public static <T> SingleFuture<T> empty() {
        return new SingleFuture<>(CompletableFuture.completedFuture(null));
    }

    /**
     * Создает SingleFuture из существующего будущего результата.
     *
     * @param completableFuture Объект, представляющий асинхронную операцию.
     * @param <T> Тип результата.
     * @return Новый SingleFuture.
     */
    public static <T> SingleFuture<T> of(CompletableFuture<T> completableFuture) {
        return new SingleFuture<>(completableFuture);
    }

    /**
     * Создает SingleFuture, завершающийся указанным значением.
     *
     * @param entity Значение, которым должен завершиться SingleFuture.
     * @param <T> Тип результата.
     * @return Новый SingleFuture.
     */
    public static <T> SingleFuture<T> of(T entity) {
        return new SingleFuture<>(CompletableFuture.completedFuture(entity));
    }

    /**
     * Создает SingleFuture, выполняющий указанного поставщика в асинхронном режиме.
     *
     * @param entitySupplier Поставщик, выполняющий асинхронную операцию.
     * @param <T> Тип результата.
     * @return Новый SingleFuture.
     */
    public static <T> SingleFuture<T> supplyAsync(Supplier<T> entitySupplier) {
        return new SingleFuture<>(CompletableFuture.supplyAsync(entitySupplier));
    }

    /**
     * Создает SingleFuture, выполняющий указанного поставщика в асинхронном
     * режиме с использованием заданного исполнителя.
     *
     * @param entitySupplier Поставщик, выполняющий асинхронную операцию.
     * @param executor Исполнитель для выполнения асинхронной операции.
     * @param <T> Тип результата.
     * @return Новый SingleFuture.
     */
    public static <T> SingleFuture<T> supplyAsync(Supplier<T> entitySupplier, Executor executor) {
        return new SingleFuture<>(CompletableFuture.supplyAsync(entitySupplier, executor));
    }

    final CompletableFuture<T> future;
    private Supplier<T> orElse;

    /**
     * Блокирует выполнение до завершения асинхронной операции и возвращает результат.
     *
     * @return Результат асинхронной операции.
     */
    public synchronized T block() {
        return future.join();
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
        return future.get();
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
     * @return Текущий SingleFuture.
     */
    public SingleFuture<T> subscribe(Runnable runnable) {
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
     * @return Текущий SingleFuture.
     */
    public SingleFuture<T> subscribe(Consumer<T> consumer) {
        if (isPresent()) {
            consumer.accept(block());
            return this;
        }
        future.thenAccept((t) -> {
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
     * @return Текущий SingleFuture.
     */
    public SingleFuture<T> subscribe(BiConsumer<T, Throwable> consumer) {
        if (isPresent()) {
            consumer.accept(block(), null);
            return this;
        }
        future.whenComplete((t, e) -> {
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
     * @return Текущий SingleFuture.
     */
    public SingleFuture<T> blockAndSubscribe(Runnable runnable) {
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
     * @return Текущий SingleFuture.
     */
    public SingleFuture<T> blockAndSubscribe(Consumer<T> consumer) {
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
     * @return Текущий SingleFuture.
     */
    public SingleFuture<T> blockAndSubscribe(BiConsumer<T, Throwable> consumer) {
        subscribe(consumer);
        block();
        return this;
    }

    /**
     * Преобразует результат асинхронной операции с помощью указанной функции.
     *
     * @param function Функция для преобразования результата.
     * @param <R> Тип результата после преобразования.
     * @return Новый SingleFuture с преобразованным результатом.
     */
    public <R> SingleFuture<R> map(Function<T, R> function) {
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
     * @return Новый SingleFuture с преобразованным результатом.
     */
    public <R> SingleFuture<R> flatMap(Function<T, Optional<R>> function) {
        return map(t -> function.apply(t).orElse(null));
    }

    /**
     * Фильтрует результат асинхронной операции с помощью указанного предиката.
     *
     * @param predicate Предикат для фильтрации результата.
     * @return Новый SingleFuture с отфильтрованным результатом.
     */
    public SingleFuture<T> filter(Predicate<T> predicate) {
        return toList().filter(predicate).first();
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
     * @return Текущий SingleFuture.
     */
    public SingleFuture<T> orElse(T def) {
        this.orElse = () -> def;
        return this;
    }

    /**
     * Устанавливает поставщика значения, которое будет возвращено, если асинхронная операция
     * завершится null значением.
     *
     * @param def Поставщик значения по умолчанию.
     * @return Текущий SingleFuture.
     */
    public SingleFuture<T> orElse(Supplier<T> def) {
        this.orElse = def;
        return this;
    }

    /**
     * Устанавливает поставщика Optional значения, которое будет возвращено, если асинхронная
     * операция завершится null значением.
     *
     * @param def Поставщик Optional значения по умолчанию.
     * @return Текущий SingleFuture.
     */
    public SingleFuture<T> or(Supplier<Optional<T>> def) {
        this.orElse = () -> def.get().orElse(null);
        return this;
    }

    /**
     * Устанавливает Optional значение, которое будет возвращено, если асинхронная операция
     * завершится null значением.
     *
     * @param def Optional значение по умолчанию.
     * @return Текущий SingleFuture.
     */
    public SingleFuture<T> or(Optional<T> def) {
        this.orElse = () -> def.orElse(null);
        return this;
    }

    /**
     * Преобразует текущий SingleFuture в ListFuture, содержащий один элемент.
     *
     * @return ListFuture, содержащий один элемент.
     */
    public ListFuture<T> toList() {
        return ListFuture.ofFutures(Collections.singletonList(future));
    }
}
