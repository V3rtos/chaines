package me.moonways.bridgenet.api.modern_command.interval;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Getter
public class IntervalInfo {

    private final long time;
    private final TimeUnit unit;
}
