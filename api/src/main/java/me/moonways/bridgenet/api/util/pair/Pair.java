package me.moonways.bridgenet.api.util.pair;

public interface Pair<K, V> {

    static <K, V> Pair<K, V> immutable(K first, V second) {
        return new ImmutablePair<>(first, second);
    }

    static <K, V> Pair<K, V> create(K first, V second) {
        return new ModifiablePair<>(first, second);
    }

    static <K, V> Pair<K, V> empty() {
        return new ModifiablePair<>();
    }

    K first();

    V second();

    K left();

    V right();

    K first(K k);

    V second(V v);

    K left(K k);

    V right(V v);

    default boolean hasFirst() {
        return first() != null;
    }

    default boolean hasSecond() {
        return first() != null;
    }

    default boolean hasLeft() {
        return left() != null;
    }

    default boolean hasRight() {
        return second() != null;
    }

    default boolean isEmpty() {
        return !hasFirst() && !hasSecond();
    }
}
