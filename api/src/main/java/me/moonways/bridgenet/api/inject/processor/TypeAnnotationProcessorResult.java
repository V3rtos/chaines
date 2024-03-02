package me.moonways.bridgenet.api.inject.processor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.bean.Bean;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@RequiredArgsConstructor
public final class TypeAnnotationProcessorResult<T> {

    @Getter
    private final Class<? extends Annotation>[] annotationsTypes;
    @Getter
    private final Class<T> type;

    private final List<Bean> beans;

    /**
     * Получить нативный список обнаруженных
     * сканированием бинов.
     */
    public List<Bean> toBeansList() {
        return beans;
    }

    /**
     * Преобразовать обнаруженные сканированием бины
     * в список объектов ожидаемого в результате типа.
     */
    public List<Class<T>> toClassesList() {
        return toList().stream().map(object -> (Class<T>) object.getClass()).collect(Collectors.toList());
    }

    /**
     * Преобразовать обнаруженные сканированием бины
     * в список объектов ожидаемого в результате типа.
     */
    public List<T> toList() {
        return beans.stream().map(Bean::getRoot).map(type::cast).collect(Collectors.toList());
    }
}
