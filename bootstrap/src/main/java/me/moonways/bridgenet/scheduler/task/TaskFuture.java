package me.moonways.bridgenet.scheduler.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class TaskFuture {

    @Getter
    private final ScheduledTask scheduledTask;

    @Delegate
    private final TaskProcessFollower taskProcessFollower;
}
