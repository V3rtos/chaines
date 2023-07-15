package me.moonways.bridgenet.injection.scanner.controller;

import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.scanner.ScannerFilter;

import java.util.Set;

public interface ScannerController {

    Set<Class<?>> findAllComponents(ScannerFilter filter);

    void whenFound(DependencyInjection dependencyInjection, Class<?> resource);
}
