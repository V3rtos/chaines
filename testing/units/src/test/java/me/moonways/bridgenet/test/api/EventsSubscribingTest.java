package me.moonways.bridgenet.test.api;

import me.moonways.bridgenet.api.event.EventService;
import me.moonways.bridgenet.api.event.subscribe.EventSubscribeBuilder;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.test.data.EntityStatus;
import me.moonways.bridgenet.test.data.EntityUser;
import me.moonways.bridgenet.test.data.ExampleUserEvent;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.data.junit.assertion.DataAssert;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.module.impl.EventsModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ModernTestEngineRunner.class)
@TestModules(EventsModule.class)
public class EventsSubscribingTest {

    @Inject
    private EventService subj;

    @Test
    public void test_successHandling() {
        subj.subscribe(
                EventSubscribeBuilder.newBuilder(ExampleUserEvent.class)
                        .follow(DataAssert::assertEvent)
                        .build());
        subj.fireEvent(
                new ExampleUserEvent(
                        EntityUser.builder()
                                .status(EntityStatus.builder()
                                        .name(TestConst.Entity.STATUS_NAME)
                                        .build())
                                .firstName(TestConst.Entity.FIRST_NAME)
                                .lastName(TestConst.Entity.LAST_NAME)
                                .age(TestConst.Entity.AGE)
                                .build()));
    }
}
