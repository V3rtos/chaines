package me.moonways.bridgenet.metrics;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.*;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class Metric {

    @EqualsAndHashCode.Include
    private final UUID id;
    @EqualsAndHashCode.Include
    private final String name;

    private final Collection<MetricValue> values = Collections.synchronizedList(new ArrayList<>());

    public Metric put(String label, long value) {
        values.add(new MetricValue(label, value, System.nanoTime()));
        return this;
    }

    public Metric add(String label, long value) {
        return put(label, (get(label) + value));
    }

    public Metric subtract(String label, long value) {
        return put(label, (get(label) - value));
    }

    public Metric removeAll(String label) {
        values.removeIf(value -> value.getLabel().equalsIgnoreCase(label));
        return this;
    }

    public Long get(String label) {
        return values.stream().filter(value -> value.getLabel().equalsIgnoreCase(label))
                .max(Comparator.comparingLong(MetricValue::getTimestamp))
                .map(MetricValue::getValue)
                .orElse(0L);
    }
}
