package me.moonways.bridgenet.test.connector;

import me.moonways.bridgenet.api.command.CommandExecutor;
import me.moonways.bridgenet.api.command.exception.CommandExecutionException;
import me.moonways.bridgenet.api.command.sender.ConsoleCommandSender;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.test.connector.subj.TestConnector;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(BridgenetJUnitTestRunner.class)
public class ConnectorHandshakeTest {

    private final TestConnector subj = new TestConnector();

    @Inject
    private CommandExecutor commandExecutor;
    @Inject
    private ConsoleCommandSender consoleCommandSender;

    @Before
    public void setUp() {
        subj.doConnectBasically();
    }

    @Test
    public void test_handshakeSuccess() {
        assertNotNull(subj.getServerUuid());
        command();
    }

    @Test
    public void test_handshakeFailed() {
        Handshake.Result result = subj.retryHandshakeExchanging();
        assertTrue(result instanceof Handshake.Failure);
    }

    private void command() {
        try {
            commandExecutor.execute(consoleCommandSender, "servers list");
            commandExecutor.execute(consoleCommandSender, "servers get " + TestConnector.DEVICE_DESCRIPTION.getName());
        } catch (CommandExecutionException exception) {
            fail(exception.getMessage());
        }
    }
}
