package me.moonways.endpoint.gui;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode(callSuper = false)
public final class BnmgFile extends File {

    private static final long serialVersionUID = -7083465806660244536L;

    BnmgFile(@NotNull String pathname) {
        super(pathname);
    }

    @SneakyThrows
    public List<String> toContentLinesList() {
        try (Stream<String> stream = Files.lines(toPath())) {
            return stream.collect(Collectors.toList());
        }
    }
}
