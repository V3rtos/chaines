package me.moonways.bridgenet.api.inject;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Класс Weak обеспечивает ленивую инициализацию объектов с использованием слабых ссылок.
 *
 * @param <T> тип объекта, который будет храниться в слабой ссылке.
 */
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Prototype<T> {

    public static <T> Prototype<T> of(@Nullable Supplier<T> supplier) {
        return new Prototype<>(supplier);
    }

    public static <T> Prototype<T> of(@Nullable T obj) {
        return new Prototype<>(() -> obj);
    }

    public static <T> Prototype<T> empty() {
        return new Prototype<>(null);
    }

    private Supplier<T> supplier;

    public synchronized Optional<T> toOptional() {
        return Optional.ofNullable(get());
    }

    public synchronized @Nullable T get() {
        return Optional.ofNullable(supplier).map(Supplier::get).orElse(null);
    }

    public synchronized void set(@Nullable Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public synchronized void set(@Nullable T obj) {
        set(() -> obj);
    }

    public boolean isPresent() {
        return get() != null;
    }
}
