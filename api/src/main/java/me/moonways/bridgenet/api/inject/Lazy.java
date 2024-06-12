package me.moonways.bridgenet.api.inject;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.bean.factory.FactoryType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Класс Lazy обеспечивает ленивую инициализацию объектов.
 *
 * @param <T> тип объекта, который будет лениво инициализирован.
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Lazy<T> {

    /**
     * Создает новый экземпляр Lazy с использованием класса и фабрики по умолчанию.
     *
     * @param <T> тип объекта, который будет лениво инициализирован.
     * @param cls класс объекта, который будет лениво инициализирован.
     * @return новый экземпляр Lazy.
     */
    public static <T> Lazy<T> ofFactory(@NotNull Class<T> cls) {
        return of(cls, () ->
                Optional.ofNullable(FactoryType.DEFAULT)
                        .orElse(FactoryType.CONSTRUCTOR).get().create(cls));
    }

    /**
     * Создает новый экземпляр Lazy с использованием указанного класса и фабрики.
     *
     * @param <T> тип объекта, который будет лениво инициализирован.
     * @param cls класс объекта, который будет лениво инициализирован.
     * @param provider поставщик фабрики для создания объекта.
     * @return новый экземпляр Lazy.
     */
    public static <T> Lazy<T> ofFactory(@NotNull Class<T> cls, @NotNull FactoryType provider) {
        return of(cls, () -> provider.get().create(cls));
    }

    /**
     * Создает новый экземпляр Lazy с использованием указанного класса и фабрики.
     *
     * @param <T> тип объекта, который будет лениво инициализирован.
     * @param cls класс объекта, который будет лениво инициализирован.
     * @param factory поставщик, создающий объект.
     * @return новый экземпляр Lazy.
     */
    public static <T> Lazy<T> of(@NotNull Class<T> cls, @NotNull Supplier<T> factory) {
        return new Lazy<>(cls, factory);
    }

    @ToString.Include
    private final Class<T> cls;
    private final Supplier<T> factory;

    @EqualsAndHashCode.Include
    @ToString.Include
    private volatile T subj;

    private Consumer<T> subscribe;
    private Consumer<T> init;

    /**
     * Возвращает лениво инициализированный объект. Если объект еще не инициализирован, он будет создан с использованием фабрики.
     *
     * @return лениво инициализированный объект.
     */
    @NotNull
    public T get() {
        if (subj == null) {
            synchronized (cls) {
                subj = factory.get();

                if (init != null) {
                    init.accept(subj);
                }
            }
        }
        if (subscribe != null) {
            synchronized (cls) {
                subscribe.accept(subj);
            }
        }
        return subj;
    }

    /**
     * Преобразовать имеющийся экземпляр на актуальный
     * момент в стандартную обертку Optional.
     */
    public Optional<T> toOptional() {
        return Optional.ofNullable(subj);
    }

    /**
     * Проверить, было ли ранее уже получено значение.
     *
     * @return true если значение уже проинициализировано, в противном случае - false.
     */
    public boolean isPresent() {
        return subj != null;
    }

    /**
     * Устанавливает обработчик, который будет вызван при получении объекта.
     *
     * @param consumer обработчик, который будет вызван при получении объекта.
     */
    public Lazy<T> subscribe(@NotNull Consumer<T> consumer) {
        if (subscribe == null) {
            subscribe = consumer;
        } else {
            subscribe = subscribe.andThen(consumer);
        }
        return this;
    }

    /**
     * Устанавливает обработчик, который будет вызван при создании объекта.
     *
     * @param consumer обработчик, который будет вызван при создании объекта.
     */
    public Lazy<T> whenInit(@NotNull Consumer<T> consumer) {
        if (init == null) {
            init = consumer;
        } else {
            init = init.andThen(consumer);
        }
        return this;
    }
}
