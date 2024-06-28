package me.moonways.bridgenet.test.api.event;

import me.moonways.bridgenet.api.event.EventService;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.test.data.EntityStatus;
import me.moonways.bridgenet.test.data.EntityUser;
import me.moonways.bridgenet.test.data.ExampleUserEvent;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.component.module.impl.EventsModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ModernTestEngineRunner.class)
@TestModules(EventsModule.class)
public class EventsListenerRegistrationTest {

    @Inject
    private EventService subj;

    @Test
    public void test_successHandling() {
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
