package me.moonways.bridgenet.api.inject;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Optional;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Singleton<T> {

    public static <T> Singleton<T> of(T instance) {
        return new Singleton<>(instance);
    }

    @EqualsAndHashCode.Include
    @ToString.Include
    private final T subj;

    /**
     * Возвращает лениво инициализированный объект. Если объект еще не инициализирован, он будет создан с использованием фабрики.
     *
     * @return лениво инициализированный объект.
     */
    public T get() {
        synchronized (subj) {
            return subj;
        }
    }

    /**
     * Преобразовать имеющийся экземпляр на актуальный
     * момент в стандартную обертку Optional.
     */
    public Optional<T> toOptional() {
        return Optional.of(subj);
    }
}
