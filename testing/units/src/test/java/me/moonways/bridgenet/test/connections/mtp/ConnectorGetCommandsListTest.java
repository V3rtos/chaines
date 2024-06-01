package me.moonways.bridgenet.test.connections.mtp;

import me.moonways.bridgenet.test.data.ExampleClient;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.module.impl.AllModules;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(ModernTestEngineRunner.class)
@TestModules(AllModules.class)
public class ConnectorGetCommandsListTest {

    private final ExampleClient subj = new ExampleClient();

    @Before
    public void setUp() {
        subj.start();
    }

    @Test
    public void test_lookupCommands() {
        List<String> commandsList = subj.lookupBridgenetRegisteredComamndsList();

        assertFalse(commandsList.isEmpty());
        assertTrue(commandsList.contains("bridgenet"));
    }
}
