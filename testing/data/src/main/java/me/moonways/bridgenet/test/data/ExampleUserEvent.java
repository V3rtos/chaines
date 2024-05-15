package me.moonways.bridgenet.test.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;

@Getter
@ToString
@RequiredArgsConstructor
public class ExampleUserEvent implements Event {
    private final EntityUser user;
}
