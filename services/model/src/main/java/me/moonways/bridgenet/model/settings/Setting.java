package me.moonways.bridgenet.model.settings;

import me.moonways.bridgenet.api.util.ExceptionallyConsumer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Интерфейс, отвечающий за шаблон поведения
 * пользовательской настройки относительно пользователя,
 * из которого мы ее получили.
 *
 * @param <T> - Тип настраиваемого объекта.
 */
public interface Setting<T> extends Remote {

// ================================= // DISABLED VALUES // ========================================================== //

    Object DISABLED_OBJECT = null;
    Boolean DISABLED_BOOLEAN = false;
    Number DISABLED_NUMBER = -1;

// ================================================================================================================== //

    /**
     * Скопировать текущий объект настройки и вернуть его клон.
     */
    Setting<T> copy() throws RemoteException;

    /**
     * Идентификатор пользовательской настройки.
     */
    SettingID<T> id() throws RemoteException;

    /**
     * Получить актуальное значение пользовательской настройки.
     */
    T get() throws RemoteException;

    /**
     * Получить актуальное значение пользовательской настройки.
     * В случае же если она не найдена у пользователя, то функция
     * вернет fallback значение, прописанное в параметре.
     *
     * @param orElse - fallback значение.
     */
    T orElse(T orElse) throws RemoteException;

    /**
     * Получить актуальное значение пользовательской настройки.
     * В случае же если она не найдена у пользователя, то функция
     * вернет fallback значение, прописанное в параметре.
     *
     * @param orElse - fallback значение.
     */
    T orElse(Supplier<T> orElse) throws RemoteException;

    /**
     * Изменить актуальное значение пользовательской настройки на новое.
     * @param value - новое значение.
     */
    Setting<T> set(T value) throws RemoteException;

    /**
     * Изменить актуальное значение пользовательской настройки на новое.
     * @param value - новое значение.
     */
    Setting<T> set(Supplier<T> value) throws RemoteException;

    /**
     * Преобразовать значение пользовательской настройки в
     * другое.
     *
     * @param function - маппер значения.
     */
    <R> Setting<R> map(Function<T, R> function) throws RemoteException;

    /**
     * Данная функция применяет вводных консумер только в том случае,
     * если пользовательская настройка была включена относительно
     * нашего пользователя.
     *
     * @param enabledConsumer - консумер.
     */
    Setting<T> ifEnabled(Consumer<T> enabledConsumer) throws RemoteException;

    /**
     * Проверить, включена ли пользовательская настройка
     * относительно пользователя, из которого мы ее получили.
     */
    boolean isEnabled() throws RemoteException;

    /**
     * Подписка на изменения значения инстанса
     * данной настройки.
     *
     * @param subscriber - обработчик входящего изменения значения.
     */
    void onChanged(ExceptionallyConsumer<T> subscriber) throws RemoteException;
}
