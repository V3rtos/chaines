package me.moonways.bridgenet.test.testengine;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.event.EventService;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.persistance.BeforeAll;
import me.moonways.bridgenet.test.engine.persistance.TestExternal;
import me.moonways.bridgenet.test.engine.persistance.TestOrdered;
import me.moonways.bridgenet.test.engine.persistance.TestSleeping;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
public class ModernTestEngineTest {

    @Inject
    private DatabaseProvider databaseProvider;
    @Inject
    private EventService eventService;

    @BeforeAll
    public void setUp() {
        log.debug("Setting up!");
    }

    @Test
    @TestOrdered(1)
    public void test_dependenciesAssertion() {
        log.debug("Asserting injectable dependencies");

        assertNull(eventService);
        assertNotNull(databaseProvider);
    }

    @Test
    @TestOrdered(2)
    @TestSleeping(3000)
    public void test_sleepingTimeout() {
        log.debug("Executing test function with 3000ms timeout!");
    }
}
