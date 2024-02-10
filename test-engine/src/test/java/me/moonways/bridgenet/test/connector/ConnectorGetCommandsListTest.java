package me.moonways.bridgenet.test.connector;

import me.moonways.bridgenet.test.connector.subj.TestConnector;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(BridgenetJUnitTestRunner.class)
public class ConnectorGetCommandsListTest {

    private final TestConnector subj = new TestConnector();

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
