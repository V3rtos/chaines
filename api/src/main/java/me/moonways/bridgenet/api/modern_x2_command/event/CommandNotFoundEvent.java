package me.moonways.bridgenet.api.modern_x2_command.event;

import lombok.Getter;
import lombok.Setter;
import me.moonways.bridgenet.api.event.Event;

@Setter
@Getter
public class CommandNotFoundEvent implements Event {

    private String message;
}
