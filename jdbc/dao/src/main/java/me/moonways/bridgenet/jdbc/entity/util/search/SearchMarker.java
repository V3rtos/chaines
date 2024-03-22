package me.moonways.bridgenet.jdbc.entity.util.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public final class SearchMarker<T> {

    public static final int UNLIMITED_LIMIT = -1;

    private final Class<T> entityClass;
    private EntityMethodHandler proxy;

    @Getter
    private int limit;

    private void prepareProxy() {
        if (proxy == null) {
            // todo
        }
    }

    public SearchMarker<T> withLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public <E> SearchMarker<T> withGet(Function<T, E> parameterGetter, E expected) {
        prepareProxy();
        //todo
        return this;
    }

    public SearchMarker<T> with(String name, Object expectedValue) {
        prepareProxy();
        //todo
        return this;
    }
}
