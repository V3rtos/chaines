package me.moonways.bridgenet.model.commands;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;

@Getter
@Builder
@ToString
public class CommandProcessEvent implements Event {

    public enum Interval {
        BEFORE_EXECUTION,
        POST_EXECUTION,
    }

    private final Interval interval;
    private final Command command;
    private final String label;
    //private final EntityAudience sender;
}
