package me.moonways.bridgenet.api.modern_command.interval;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Getter
public class IntervalInfoImpl implements IntervalInfo {

    private final String userName;

    private final long number;
    private final TimeUnit time;
}
