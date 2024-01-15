package me.moonways.bridgenet.api.command.wrapper;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.command.exception.CommandArgumentsException;
import me.moonways.bridgenet.api.command.exception.CommandExecutionException;
import me.moonways.bridgenet.api.util.ExceptionallyFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public final class WrappedArguments implements Iterable<String> {

    private final String[] handle;

    public Stream<String> stream() {
        return Arrays.stream(handle);
    }

    public String[] toStringArray() {
        return handle;
    }

    private Optional<String> lookup(int position) {
        if (isEmpty() || size() < position) {
            return Optional.empty();
        }

        return Optional.ofNullable(handle[position]);
    }

    public Optional<String> get(int position) {
        return lookup(position);
    }

    public <R> Optional<R> get(int position, ExceptionallyFunction<String, R> mapper) {
        try {
            return Optional.ofNullable(mapper.apply(lookup(position).orElse(null)));
        } catch (Throwable exception) {
            throw new CommandArgumentsException(exception);
        }
    }

    public Optional<String> first() {
        return get(0);
    }

    public <R> Optional<R> first(ExceptionallyFunction<String, R> mapper) {
        return get(0, mapper);
    }

    public Optional<String> last() {
        if (size() - 1 < 0) {
            return Optional.empty();
        }
        return get(size() - 1);
    }

    public <R> Optional<R> last(ExceptionallyFunction<String, R> mapper) {
        if (size() - 1 < 0) {
            return Optional.empty();
        }
        return get(size() - 1, mapper);
    }

    public boolean isEmpty() {
        return handle.length == 0;
    }

    public int size() {
        return handle.length;
    }

    public void assertSize(int required) {
        if (!has(required)) {
            throw new AssertionError(String.format("Illegal command arguments size (%d < %d)", size(), required));
        }
    }

    public boolean has(int requiredSize) {
        return size() >= requiredSize;
    }

    @NotNull
    @Override
    public Iterator<String> iterator() {
        return Arrays.asList(handle).iterator();
    }
}
