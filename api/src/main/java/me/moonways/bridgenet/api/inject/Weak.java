package me.moonways.bridgenet.api.inject;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Optional;

/**
 * Класс Weak обеспечивает ленивую инициализацию объектов с использованием слабых ссылок.
 *
 * @param <T> тип объекта, который будет храниться в слабой ссылке.
 */
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Weak<T> {

    /**
     * Создает новый экземпляр Weak с использованием переданного объекта.
     *
     * @param <T> тип объекта, который будет храниться в слабой ссылке.
     * @param obj объект, который будет храниться в слабой ссылке.
     * @return новый экземпляр Weak.
     */
    public static <T> Weak<T> of(@Nullable T obj) {
        return new Weak<>(new WeakReference<>(obj));
    }

    /**
     * Создает пустой экземпляр Weak.
     *
     * @param <T> тип объекта, который будет храниться в слабой ссылке.
     * @return новый пустой экземпляр Weak.
     */
    public static <T> Weak<T> empty() {
        return new Weak<>(new WeakReference<>(null));
    }

    private WeakReference<T> ref;

    /**
     * Возвращает объект, хранящийся в слабой ссылке, как Optional.
     *
     * @return объект, хранящийся в слабой ссылке, в виде Optional.
     */
    public synchronized Optional<T> toOptional() {
        return Optional.ofNullable(get());
    }

    /**
     * Возвращает объект, хранящийся в слабой ссылке.
     *
     * @return объект, хранящийся в слабой ссылке.
     */
    public synchronized @Nullable T get() {
        return ref.get();
    }

    /**
     * Устанавливает новый объект для хранения в слабой ссылке.
     *
     * @param obj объект для хранения в слабой ссылке.
     */
    public synchronized void set(@Nullable T obj) {
        ref = new WeakReference<>(obj);
    }

    /**
     * Проверить, осталась ли ссылка на текущий сохраненный
     * объект в памяти.
     *
     * @return true если ссылка еще существует, в противном случае - false.
     */
    public boolean isPresent() {
        return get() != null;
    }

    /**
     * Очищает слабую ссылку.
     */
    public synchronized void clear() {
        ref.clear();
    }

    /**
     * Очищает слабую ссылку при финализации объекта.
     *
     * @throws Throwable если возникла ошибка при финализации.
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        ref.clear();
    }
}
