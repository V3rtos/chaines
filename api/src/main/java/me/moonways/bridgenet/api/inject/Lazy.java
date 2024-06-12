package me.moonways.bridgenet.api.inject;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactoryProviders;

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
    public static <T> Lazy<T> defaultFactory(Class<T> cls) {
        return factory(cls,
                Optional.ofNullable(BeanFactoryProviders.DEFAULT)
                        .orElse(BeanFactoryProviders.CONSTRUCTOR));
    }

    /**
     * Создает новый экземпляр Lazy с использованием указанного класса и фабрики.
     *
     * @param <T> тип объекта, который будет лениво инициализирован.
     * @param cls класс объекта, который будет лениво инициализирован.
     * @param provider поставщик фабрики для создания объекта.
     * @return новый экземпляр Lazy.
     */
    public static <T> Lazy<T> factory(Class<T> cls, BeanFactoryProviders provider) {
        return supply(cls, () -> provider.getImpl().get().create(cls));
    }

    /**
     * Создает новый экземпляр Lazy с использованием указанного класса и фабрики.
     *
     * @param <T> тип объекта, который будет лениво инициализирован.
     * @param cls класс объекта, который будет лениво инициализирован.
     * @param factory поставщик, создающий объект.
     * @return новый экземпляр Lazy.
     */
    public static <T> Lazy<T> supply(Class<T> cls, Supplier<T> factory) {
        return new Lazy<>(cls, factory);
    }

    @ToString.Include
    private final Class<T> cls;
    private final Supplier<T> factory;

    @EqualsAndHashCode.Include
    @ToString.Include
    private T subj;

    private Consumer<T> onGet;
    private Consumer<T> onSet;

    /**
     * Возвращает лениво инициализированный объект. Если объект еще не инициализирован, он будет создан с использованием фабрики.
     *
     * @return лениво инициализированный объект.
     */
    public synchronized T get() {
        if (subj == null) {
            subj = factory.get();
            if (onSet != null) {
                onSet.accept(subj);
            }
        }
        if (onGet != null) {
            onGet.accept(subj);
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
    public Lazy<T> subscribe(Consumer<T> consumer) {
        if (onGet == null) {
            onGet = consumer;
        } else {
            onGet = onGet.andThen(consumer);
        }
        return this;
    }

    /**
     * Устанавливает обработчик, который будет вызван при создании объекта.
     *
     * @param consumer обработчик, который будет вызван при создании объекта.
     */
    public Lazy<T> whenInit(Consumer<T> consumer) {
        if (onSet == null) {
            onSet = consumer;
        } else {
            onSet = onSet.andThen(consumer);
        }
        return this;
    }
}
