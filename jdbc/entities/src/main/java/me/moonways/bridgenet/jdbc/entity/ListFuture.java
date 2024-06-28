package me.moonways.bridgenet.jdbc.entity;

import com.google.common.collect.Iterables;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * Класс для работы с асинхронными операциями, возвращающими коллекцию результатов.
 *
 * @param <T> Тип результата асинхронных операций.
 */
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ListFuture<T> {

    /**
     * Создает пустой ListFuture, завершающийся пустой коллекцией.
     *
     * @param <T> Тип результата.
     * @return Пустой ListFuture.
     */
    public static <T> ListFuture<T> empty() {
        return new ListFuture<>(Collections.synchronizedList(new ArrayList<>()));
    }

    /**
     * Создает ListFuture из списка будущих результатов.
     *
     * @param futures Список асинхронных операций.
     * @param <T> Тип результата.
     * @return Новый ListFuture.
     */
    public static <T> ListFuture<T> ofFutures(List<CompletableFuture<T>> futures) {
        return new ListFuture<>(futures);
    }

    /**
     * Создает ListFuture из Iterable коллекции.
     *
     * @param entities Коллекция сущностей.
     * @param cls Класс типа результата.
     * @param <T> Тип результата.
     * @return Новый ListFuture.
     */
    public static <T> ListFuture<T> of(Iterable<T> entities, Class<T> cls) {
        return new ListFuture<>(Arrays.stream(Iterables.toArray(entities, cls))
                .map(CompletableFuture::completedFuture)
                .collect(Collectors.toCollection(() -> Collections.synchronizedList(new ArrayList<>()))));
    }

    /**
     * Создает ListFuture, выполняющий список поставщиков в асинхронном режиме.
     *
     * @param entitySuppliers Список поставщиков.
     * @param <T> Тип результата.
     * @return Новый ListFuture.
     */
    public static <T> ListFuture<T> supplyAsync(List<Supplier<T>> entitySuppliers) {
        return new ListFuture<>(entitySuppliers.stream()
                .map(CompletableFuture::supplyAsync)
                .collect(Collectors.toCollection(() -> Collections.synchronizedList(new ArrayList<>()))));
    }

    /**
     * Создает ListFuture, выполняющий список поставщиков в асинхронном режиме
     * с использованием заданного исполнителя.
     *
     * @param entitySuppliers Список поставщиков.
     * @param executor Исполнитель для выполнения асинхронных операций.
     * @param <T> Тип результата.
     * @return Новый ListFuture.
     */
    public static <T> ListFuture<T> supplyAsync(List<Supplier<T>> entitySuppliers, Executor executor) {
        return new ListFuture<>(entitySuppliers.stream()
                .map(supplier -> CompletableFuture.supplyAsync(supplier, executor))
                .collect(Collectors.toCollection(() -> Collections.synchronizedList(new ArrayList<>()))));
    }

    private final List<CompletableFuture<T>> futures;

    /**
     * Возвращает первый результат асинхронных операций в виде SingleFuture.
     *
     * @return Первый результат в виде SingleFuture.
     */
    public SingleFuture<T> first() {
        return Optional.ofNullable(Iterables.getFirst(futures, null))
                .map(SingleFuture::of)
                .orElseGet(SingleFuture::empty);
    }

    /**
     * Возвращает последний результат асинхронных операций в виде SingleFuture.
     *
     * @return Последний результат в виде SingleFuture.
     */
    public SingleFuture<T> last() {
        return Optional.ofNullable(Iterables.getLast(futures, null))
                .map(SingleFuture::of)
                .orElseGet(SingleFuture::empty);
    }

    /**
     * Блокирует выполнение до завершения всех асинхронных операций
     * и возвращает список результатов.
     *
     * @return Список результатов асинхронных операций.
     */
    public synchronized List<T> blockAll() {
        return futures.stream()
                .map(SingleFuture::of)
                .peek(SingleFuture::block)
                .filter(futures::contains)
                .map(SingleFuture::block)
                .collect(Collectors.toList());
    }

    /**
     * Блокирует выполнение до завершения всех асинхронных операций и возвращает
     * коллекцию результатов.
     *
     * @param collectionSupplier Поставщик коллекции для хранения результатов.
     * @param <C> Тип коллекции.
     * @return Коллекция результатов асинхронных операций.
     */
    public <C extends Collection<T>> C blockAll(Supplier<C> collectionSupplier) {
        return futures.stream()
                .map(SingleFuture::of)
                .peek(SingleFuture::block)
                .filter(futures::contains)
                .map(SingleFuture::block)
                .collect(Collectors.toCollection(collectionSupplier));
    }

    /**
     * Блокирует выполнение до завершения всех асинхронных операций
     * и возвращает список результатов в виде Optional.
     *
     * @return Optional со списком результатов.
     */
    public synchronized Optional<List<T>> blockOptional() {
        return blockOptional(null);
    }

    /**
     * Блокирует выполнение до завершения первой асинхронной операции
     * и возвращает результат.
     *
     * @return Результат первой асинхронной операции.
     */
    public synchronized T blockFirst() {
        return first().block();
    }

    /**
     * Блокирует выполнение до завершения первой асинхронной операции
     * и возвращает результат в виде Optional.
     *
     * @return Optional с результатом первой асинхронной операции.
     */
    public synchronized Optional<T> blockFirstOptional() {
        return first().blockOptional();
    }

    /**
     * Блокирует выполнение до завершения последней асинхронной операции
     * и возвращает результат.
     *
     * @return Результат последней асинхронной операции.
     */
    public synchronized T blockLast() {
        return last().block();
    }

    /**
     * Блокирует выполнение до завершения последней асинхронной операции
     * и возвращает результат в виде Optional.
     *
     * @return Optional с результатом последней асинхронной операции.
     */
    public synchronized Optional<T> blockLastOptional() {
        return last().blockOptional();
    }

    /**
     * Блокирует выполнение до завершения всех асинхронных операций,
     * обрабатывая исключения.
     *
     * @param exceptionConsumer Обработчик исключений.
     * @return Optional со списком результатов.
     */
    public synchronized Optional<List<T>> blockOptional(Consumer<Throwable> exceptionConsumer) {
        try {
            return Optional.ofNullable(blockAll());
        } catch (Throwable exception) {
            if (exceptionConsumer != null) {
                exceptionConsumer.accept(exception);
            }
            return Optional.empty();
        }
    }

    /**
     * Блокирует выполнение до завершения всех асинхронных операций
     * и возвращает список результатов.
     *
     * @return Список результатов асинхронных операций.
     */
    public List<T> freezeAll() {
        return futures.stream()
                .map(SingleFuture::of)
                .peek(SingleFuture::freeze)
                .filter(futures::contains)
                .map(SingleFuture::block)
                .collect(Collectors.toList());
    }

    /**
     * Блокирует выполнение до завершения всех асинхронных операций с указанным
     * таймаутом и возвращает список результатов.
     *
     * @param timeout Таймаут ожидания.
     * @return Список результатов асинхронных операций.
     */
    public List<T> freezeAll(Duration timeout) {
        return futures.stream()
                .map(SingleFuture::of)
                .peek(future -> future.freeze(timeout))
                .filter(futures::contains)
                .map(SingleFuture::block)
                .collect(Collectors.toList());
    }

    /**
     * Подписывается на выполнение каждой асинхронной операции, выполняя указанный Runnable
     * после завершения.
     *
     * @param runnable Runnable для выполнения после завершения.
     * @return Текущий ListFuture.
     */
    public ListFuture<T> subscribeEach(Runnable runnable) {
        futures.stream().map(SingleFuture::of).forEach(future -> {
            if (futures.contains(future.future))
                future.subscribe(runnable);
        });
        return this;
    }

    /**
     * Подписывается на выполнение каждой асинхронной операции, принимая результат в указанный
     * Consumer после завершения.
     *
     * @param consumer Consumer для обработки результата.
     * @return Текущий ListFuture.
     */
    public ListFuture<T> subscribeEach(Consumer<T> consumer) {
        futures.stream().map(SingleFuture::of).forEach(future -> {
            if (futures.contains(future.future))
                future.subscribe(consumer);
        });
        return this;
    }

    /**
     * Подписывается на выполнение каждой асинхронной операции, принимая результат и исключение
     * в указанный BiConsumer после завершения.
     *
     * @param consumer BiConsumer для обработки результата и исключения.
     * @return Текущий ListFuture.
     */
    public ListFuture<T> subscribeEach(BiConsumer<T, Throwable> consumer) {
        futures.stream().map(SingleFuture::of).forEach(future -> {
            if (futures.contains(future.future))
                future.subscribe(consumer);
        });
        return this;
    }

    /**
     * Подписывается на выполнение первой асинхронной операции, выполняя указанный Runnable
     * после завершения.
     *
     * @param runnable Runnable для выполнения после завершения.
     * @return Текущий ListFuture.
     */
    public ListFuture<T> subscribeFirst(Runnable runnable) {
        CompletableFuture<T> first = Iterables.getFirst(futures, null);
        if (first != null) {
            SingleFuture.of(first)
                    .subscribe(runnable);
        }
        return this;
    }

    /**
     * Подписывается на выполнение первой асинхронной операции, принимая результат в указанный
     * Consumer после завершения.
     *
     * @param consumer Consumer для обработки результата.
     * @return Текущий ListFuture.
     */
    public ListFuture<T> subscribeFirst(Consumer<T> consumer) {
        CompletableFuture<T> first = Iterables.getFirst(futures, null);
        if (first != null) {
            SingleFuture.of(first)
                    .subscribe(consumer);
        }
        return this;
    }

    /**
     * Подписывается на выполнение первой асинхронной операции, принимая результат и исключение
     * в указанный BiConsumer после завершения.
     *
     * @param consumer BiConsumer для обработки результата и исключения.
     * @return Текущий ListFuture.
     */
    public ListFuture<T> subscribeFirst(BiConsumer<T, Throwable> consumer) {
        CompletableFuture<T> first = Iterables.getFirst(futures, null);
        if (first != null) {
            SingleFuture.of(first)
                    .subscribe(consumer);
        }
        return this;
    }

    /**
     * Подписывается на выполнение последней асинхронной операции, выполняя указанный Runnable
     * после завершения.
     *
     * @param runnable Runnable для выполнения после завершения.
     * @return Текущий ListFuture.
     */
    public ListFuture<T> subscribeLast(Runnable runnable) {
        CompletableFuture<T> last = Iterables.getLast(futures, null);
        if (last != null) {
            SingleFuture.of(last)
                    .subscribe(runnable);
        }
        return this;
    }

    /**
     * Подписывается на выполнение последней асинхронной операции, принимая результат в указанный
     * Consumer после завершения.
     *
     * @param consumer Consumer для обработки результата.
     * @return Текущий ListFuture.
     */
    public ListFuture<T> subscribeLast(Consumer<T> consumer) {
        CompletableFuture<T> last = Iterables.getLast(futures, null);
        if (last != null) {
            SingleFuture.of(last)
                    .subscribe(consumer);
        }
        return this;
    }

    /**
     * Подписывается на выполнение последней асинхронной операции, принимая результат и исключение
     * в указанный BiConsumer после завершения.
     *
     * @param consumer BiConsumer для обработки результата и исключения.
     * @return Текущий ListFuture.
     */
    public ListFuture<T> subscribeLast(BiConsumer<T, Throwable> consumer) {
        CompletableFuture<T> last = Iterables.getLast(futures, null);
        if (last != null) {
            SingleFuture.of(last)
                    .subscribe(consumer);
        }
        return this;
    }

    /**
     * Блокирует выполнение до завершения всех асинхронных операций и выполняет указанный Runnable.
     *
     * @param runnable Runnable для выполнения после завершения всех операций.
     * @return Текущий ListFuture.
     */
    public ListFuture<T> blockAndSubscribeAll(Runnable runnable) {
        blockAll();
        runnable.run();
        return this;
    }

    /**
     * Блокирует выполнение до завершения всех асинхронных операций и выполняет указанный
     * Runnable для каждого результата.
     *
     * @param runnable Runnable для выполнения после завершения каждой операции.
     * @return Текущий ListFuture.
     */
    public ListFuture<T> blockAndSubscribeEach(Runnable runnable) {
        blockAll().forEach(t -> runnable.run());
        return this;
    }

    /**
     * Блокирует выполнение до завершения всех асинхронных операций и принимает результат
     * в указанный Consumer для каждого результата.
     *
     * @param consumer Consumer для обработки каждого результата.
     * @return Текущий ListFuture.
     */
    public ListFuture<T> blockAndSubscribeEach(Consumer<T> consumer) {
        blockAll().forEach(consumer);
        return this;
    }

    /**
     * Блокирует выполнение до завершения всех асинхронных операций и принимает результат
     * и исключение в указанный BiConsumer для каждого результата.
     *
     * @param consumer BiConsumer для обработки каждого результата и исключения.
     * @return Текущий ListFuture.
     */
    public ListFuture<T> blockAndSubscribeEach(BiConsumer<T, Throwable> consumer) {
        subscribeEach(consumer);
        blockAll();
        return this;
    }

    /**
     * Фильтрует результаты асинхронных операций на основе заданного предиката.
     *
     * @param predicate Предикат для фильтрации результатов.
     * @return Текущий ListFuture с отфильтрованными результатами.
     */
    public ListFuture<T> filter(Predicate<T> predicate) {
        new ArrayList<>(futures).forEach(completableFuture -> {
            completableFuture.thenAccept(t -> {
                if (!predicate.test(t))
                    futures.remove(completableFuture);
            });
        });
        return this;
    }

    /**
     * Применяет функцию к каждому результату асинхронной операции и возвращает новый
     * ListFuture с преобразованными результатами.
     *
     * @param function Функция для преобразования результатов.
     * @param <R> Тип преобразованных результатов.
     * @return Новый ListFuture с преобразованными результатами.
     */
    public <R> ListFuture<R> mapEach(Function<T, R> function) {
        return ofFutures(futures.stream().map(SingleFuture::of).map(future -> future.map(function).future)
                .collect(Collectors.toList()));
    }

    /**
     * Применяет функцию, возвращающую Optional, к каждому результату асинхронной
     * операции и возвращает новый ListFuture с преобразованными результатами.
     *
     * @param function Функция для преобразования результатов, возвращающая Optional.
     * @param <R> Тип преобразованных результатов.
     * @return Новый ListFuture с преобразованными результатами.
     */
    public <R> ListFuture<R> flatMapEach(Function<T, Optional<R>> function) {
        return ofFutures(futures.stream().map(SingleFuture::of).map(future -> future.flatMap(function).future)
                .collect(Collectors.toList()));
    }

    /**
     * Проверяет, завершены ли все асинхронные операции.
     *
     * @return true, если все операции завершены, иначе false.
     */
    public boolean isPresentAll() {
        return futures.stream().allMatch(CompletableFuture::isDone);
    }

    /**
     * Добавляет SingleFuture к текущему ListFuture.
     *
     * @param other SingleFuture для добавления.
     * @return Текущий ListFuture.
     */
    public ListFuture<T> add(SingleFuture<T> other) {
        futures.add(other.future);
        return this;
    }

    /**
     * Добавляет все элементы из другого ListFuture к текущему ListFuture.
     *
     * @param other ListFuture для добавления.
     * @return Текущий ListFuture.
     */
    public ListFuture<T> addAll(ListFuture<T> other) {
        futures.addAll(other.futures);
        return this;
    }

    /**
     * Возвращает размер текущего ListFuture.
     *
     * @return Размер ListFuture.
     */
    public int size() {
        return futures.size();
    }

    /**
     * Проверяет, пуст ли текущий ListFuture.
     *
     * @return true, если ListFuture пуст, иначе false.
     */
    public boolean isEmpty() {
        return futures.isEmpty();
    }
}
