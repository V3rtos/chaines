package me.moonways.bridgenet.test.client;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.test.data.ExampleClient;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.component.module.impl.ClientsModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(ModernTestEngineRunner.class)
@TestModules(ClientsModule.class)
public class ClientGetCommandsListTest {

    @Inject
    private ExampleClient subj;

    @Test
    public void test_lookupCommands() {
        List<String> commandsList = subj.lookupBridgenetRegisteredComamndsList();

        assertFalse(commandsList.isEmpty());
        assertTrue(commandsList.contains("bridgenet"));
    }
}
