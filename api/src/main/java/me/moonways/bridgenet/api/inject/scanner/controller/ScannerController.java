package me.moonways.bridgenet.api.inject.scanner.controller;

import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.scanner.ScannerFilter;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Контроллер, отвечающий за сканирование проекта,
 * поиск и обработку найденных ресурсов.
 */
public interface ScannerController {

    /**
     * Запросить список доступных ресурсов в системе,
     * подходящих по параметрам фильтра.
     *
     * @param filter - фильтр запроса.
     */
    Set<Class<?>> requestResources(@NotNull ScannerFilter filter);

    /**
     * Обработать найденный ресурс данным контроллером.
     *
     * @param injector - реализация DependencyInjection.
     * @param resource - найденный ресурс
     * @param annotation - аннотация, по которому нашли данный ресурс.
     */
    void handleResource(@NotNull DependencyInjection injector,
                        @NotNull Class<?> resource,
                        @NotNull Class<? extends Annotation> annotation);
}
