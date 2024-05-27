package me.moonways.rsap.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.net.SocketAddress;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StubAccess<T> {

    public static <T> StubAccess<T> fromRemoteEntity(SocketAddress address, T entity) {
        return new StubAccess<>(address, entity);
    }

    private final SocketAddress address;
    private final T entity;

    public Optional<T> getStub() {
        return Optional.ofNullable(entity);
    }

    public boolean isExported() {
        return getStub().isPresent();
    }

    @Override
    public String toString() {
        String entityName = entity != null ? entity.getClass().getSimpleName() : "UNEXPORTED";
        return String.format("%s{bindingOn=%s}", entityName, address);
    }
}
