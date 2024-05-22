package me.moonways.bridgenet.api.autorun;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.scheduler.ScheduledTime;
import me.moonways.bridgenet.api.util.ExceptionallyRunnable;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class RunnableUnit {

    @EqualsAndHashCode.Include
    private final UUID uuid = UUID.randomUUID();

    private final ScheduledTime period;
    private final ExceptionallyRunnable runnable;
}
