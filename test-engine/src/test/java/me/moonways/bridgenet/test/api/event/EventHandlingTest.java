package me.moonways.bridgenet.test.api.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.api.event.EventManager;
import me.moonways.bridgenet.api.event.subscribe.EventSubscribeBuilder;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(BridgenetJUnitTestRunner.class)
public class EventHandlingTest {

    private final UserConnectEvent eventToSend = new UserConnectEvent(UUID.randomUUID(), "Mike Tyson");

    @Inject
    private EventManager eventManager;

    private void assertInputEvent(UserConnectEvent event) {
        assertEquals(event.getName(), eventToSend.getName());
        assertEquals(event.getUuid(), eventToSend.getUuid());
    }

    @Test
    public void test_successHandling() {
        eventManager.subscribe(
                EventSubscribeBuilder.newBuilder(UserConnectEvent.class)
                        .follow(this::assertInputEvent)
                        .build());

        eventManager.fireEvent(eventToSend);
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    private static class UserConnectEvent implements Event {

        private final UUID uuid;
        private final String name;
    }
}
