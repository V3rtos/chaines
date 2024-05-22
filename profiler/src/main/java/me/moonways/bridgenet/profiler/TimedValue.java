package me.moonways.bridgenet.profiler;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class TimedValue {

    private final String label;
    private final Long value;

    private final long timestamp;
}
