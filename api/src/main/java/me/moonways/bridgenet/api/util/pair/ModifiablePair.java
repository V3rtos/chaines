package me.moonways.bridgenet.api.util.pair;

import lombok.*;

@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ModifiablePair<K, V> implements Pair<K, V> {

    private K first;
    private V second;

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
        K prev = first;
        first = k;
        return prev;
    }

    @Override
    public V second(V v) {
        V prev = second;
        second = v;
        return prev;
    }

    @Override
    public K left(K k) {
        return first(k);
    }

    @Override
    public V right(V v) {
        return second(v);
    }
}
