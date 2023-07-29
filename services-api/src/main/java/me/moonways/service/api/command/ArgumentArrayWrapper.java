package me.moonways.service.api.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public class ArgumentArrayWrapper {

    private final String[] args;

    public Stream<String> stream() {
        return Arrays.stream(args);
    }

    public <R> R map(int position, Function<String, R> mapper) {
        return mapper.apply(args[position]);
    }

    public String get(int position) {
        return args[position];
    }

    public String getFirst() {
        return args[0];
    }

    public String getSecond() {
        return args[1];
    }

    public String getThird() {
        return args[2];
    }

    public String getLast() {
        return args[args.length - 1];
    }
}
