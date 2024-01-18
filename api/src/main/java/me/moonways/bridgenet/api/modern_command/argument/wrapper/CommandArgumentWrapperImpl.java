package me.moonways.bridgenet.api.modern_command.argument.wrapper;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_command.argument.CommandArgumentsException;
import me.moonways.bridgenet.api.util.ExceptionallyFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public final class CommandArgumentWrapperImpl implements CommandArgumentWrapper {

    private final String[] handle;

    @Override
    public Stream<String> stream() {
        return Arrays.stream(handle);
    }

    @Override
    public String[] toStringArray() {
        return handle;
    }

    private Optional<String> lookup(int position) {
        if (isEmpty() || size() < position) {
            return Optional.empty();
        }

        return Optional.ofNullable(handle[position]);
    }

    @Override
    public Optional<String> get(int position) {
        return lookup(position);
    }

    @Override
    public <R> Optional<R> get(int position, ExceptionallyFunction<String, R> mapper) {
        try {
            return Optional.ofNullable(mapper.apply(lookup(position).orElse(null)));
        } catch (Throwable exception) {
            throw new CommandArgumentsException(exception);
        }
    }

    @Override
    public Optional<String> first() {
        return get(0);
    }

    @Override
    public <R> Optional<R> first(ExceptionallyFunction<String, R> mapper) {
        return get(0, mapper);
    }

    @Override
    public Optional<String> last() {
        if (size() - 1 < 0) {
            return Optional.empty();
        }
        return get(size() - 1);
    }

    @Override
    public <R> Optional<R> last(ExceptionallyFunction<String, R> mapper) {
        if (size() - 1 < 0) {
            return Optional.empty();
        }
        return get(size() - 1, mapper);
    }

    @Override
    public boolean isEmpty() {
        return handle.length == 0;
    }

    @Override
    public int size() {
        return handle.length;
    }

    @Override
    public void assertSize(int required) {
        if (!has(required)) {
            throw new AssertionError(String.format("Illegal command arguments size (%d < %d)", size(), required));
        }
    }

    @Override
    public boolean has(int requiredSize) {
        return size() >= requiredSize;
    }

    @NotNull
    @Override
    public Iterator<String> iterator() {
        return Arrays.asList(handle).iterator();
    }
}
