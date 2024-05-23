package me.moonways.bridgenet.profiler;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.*;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class Profiler {

    private static final int MAX_VALUES_SIZE = 290;

    @EqualsAndHashCode.Include
    private final UUID id;
    @EqualsAndHashCode.Include
    private final String name;

    private final List<TimedValue> values = Collections.synchronizedList(new ArrayList<>());

    /**
     * Добавить новое значение метрики в кеш.
     *
     * @param label - наименование значения.
     * @param value - числовое значение кеша.
     */
    public Profiler put(String label, long value) {
        values.add(new TimedValue(label, value, System.nanoTime()));

        if (values.size() >= MAX_VALUES_SIZE)
            values.remove(0);
        return this;
    }

    /**
     * Суммировать последнее кешированное значение и
     * занести в кеш новое, с уже суммированным значением.
     *
     * @param label - наименование значения.
     * @param value - сколько необходимо добавить к актуальному значению.
     */
    public Profiler add(String label, long value) {
        return put(label, (get(label) + value));
    }

    /**
     * Отнять из последнего кешированного значения и
     * занести в кеш новое, с уже отнятым значением.
     *
     * @param label - наименование значения.
     * @param value - сколько необходимо отнять от актуального значения.
     */
    public Profiler subtract(String label, long value) {
        return put(label, (get(label) - value));
    }

    /**
     * Удалить все кеши значений, которые привязаны к
     * указанному наименованию значений.
     *
     * @param label - наименование, по которому удалить все значения.
     */
    public Profiler removeAll(String label) {
        values.removeIf(value -> value.getLabel().equalsIgnoreCase(label));
        return this;
    }

    /**
     * Получить последнее актуальное значение, которое было
     * добавлено позже всего по тайм-лайну.
     *
     * @param label - наименование значения, которое ищем.
     */
    public Long get(String label) {
        return values.stream().filter(value -> value.getLabel().equalsIgnoreCase(label))
                .max(Comparator.comparingLong(TimedValue::getTimestamp))
                .map(TimedValue::getValue)
                .orElse(0L);
    }
}
