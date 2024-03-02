package me.moonways.bridgenet.api.util.pair;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ImmutablePair<K, V> implements Pair<K, V> {

    private final K first;
    private final V second;

    @Override
    public K first() {
        return first;
    }

    @Override
    public V second() {
        return second;
    }

    @Override
    public K left() {
        return first();
    }

    @Override
    public V right() {
        return second();
    }

    @Override
    public K first(K k) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public V second(V v) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public K left(K k) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public V right(V v) {
        throw new UnsupportedOperationException("immutable");
    }
}
