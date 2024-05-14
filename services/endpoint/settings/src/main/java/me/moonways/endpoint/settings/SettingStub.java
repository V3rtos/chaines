package me.moonways.endpoint.settings;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.model.settings.Setting;
import me.moonways.bridgenet.model.settings.SettingID;

import java.rmi.RemoteException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class SettingStub<T> implements Setting<T> {

    private final SettingID<T> id;
    private T value;

    @Override
    public SettingID<T> id() {
        return id;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public boolean isEnabled() throws RemoteException {
        return !SettingStub.isDisabled(this);
    }

    @Override
    public T orElse(T orElse) {
        return Optional.ofNullable(get()).orElse(orElse);
    }

    @Override
    public T orElse(Supplier<T> orElse) {
        return Optional.ofNullable(get()).orElseGet(orElse);
    }

    @Override
    public Setting<T> set(T value) {
        this.value = value;
        return this;
    }

    @Override
    public Setting<T> set(Supplier<T> value) {
        return set(value.get());
    }

    @Override
    public Setting<T> ifEnabled(Consumer<T> consumer) throws RemoteException {
        if (isEnabled()) {
            consumer.accept(value);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> Setting<R> map(Function<T, R> function) {
        return new SettingStub<R>((SettingID<R>) id).set(function.apply(value));
    }

    public static boolean isDisabled(SettingStub<?> settingStub) {
        Class<?> type = settingStub.id().getType();

        if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return Objects.equals(settingStub.get(), DISABLED_BOOLEAN);
        }
        if (type.isPrimitive() || Number.class.isAssignableFrom(type)) {
            return Objects.equals(settingStub.get(), DISABLED_NUMBER);
        }

        return Objects.equals(settingStub.get(), DISABLED_OBJECT);
    }
}
