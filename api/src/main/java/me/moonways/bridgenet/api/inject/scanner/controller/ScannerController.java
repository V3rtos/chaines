package me.moonways.bridgenet.api.inject.scanner.controller;

import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.scanner.ScannerFilter;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface ScannerController {

    Set<Class<?>> findAllComponents(@NotNull ScannerFilter filter);

    void whenFound(@NotNull DependencyInjection dependencyInjection,
                   @NotNull Class<?> resource,
                   @NotNull Class<? extends Annotation> annotation);
}
