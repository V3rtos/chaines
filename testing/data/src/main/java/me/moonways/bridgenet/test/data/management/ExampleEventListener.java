package me.moonways.bridgenet.test.data.management;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.event.InboundEventListener;
import me.moonways.bridgenet.api.event.SubscribeEvent;
import me.moonways.bridgenet.test.data.ExampleUserEvent;

@Log4j2
@InboundEventListener
public class ExampleEventListener {

    @SubscribeEvent
    public void handle(ExampleUserEvent event) {
        log.debug(event);
    }
}
