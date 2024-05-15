package me.moonways.bridgenet.test.data.management;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.event.EventHandle;
import me.moonways.bridgenet.test.data.ExampleUserEvent;

@Log4j2
public class ExampleEventListener {

    @EventHandle
    public void handle(ExampleUserEvent event) {
        log.debug(event);
    }
}
