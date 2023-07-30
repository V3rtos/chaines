package me.moonways.bridgenet.api.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public final class ArgumentArrayWrapper {

    private final String[] nativeArray;

    public Stream<String> stream() {
        return Arrays.stream(nativeArray);
    }

    public <R> R map(int position, Function<String, R> mapper) {
        return mapper.apply(lookup(position));
    }

    private String lookup(int position) {
        if (isEmpty() || getSize() < position) {
            return null;
        }

        return nativeArray[position];
    }

    public String get(int position) {
        return lookup(position);
    }

    public Optional<String> getFirst() {
        return Optional.ofNullable(lookup(0));
    }

    public Optional<String> getSecond() {
        return Optional.ofNullable(lookup(1));
    }

    public Optional<String> getLast() {
        return Optional.ofNullable(isEmpty() ? null : nativeArray[getSize() - 1]);
    }

    public boolean isEmpty() {
        return nativeArray.length == 0;
    }

    public int getSize() {
        return nativeArray.length;
    }
}
