package me.moonways.bridgenet.test.connector;

import me.moonways.bridgenet.api.command.CommandExecutor;
import me.moonways.bridgenet.api.command.exception.CommandExecutionException;
import me.moonways.bridgenet.api.command.sender.ConsoleCommandSender;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.test.connector.subj.TestConnector;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import me.moonways.bridgenet.test.engine.persistance.Order;
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

    @Test
    @Order(0)
    public void test_handshakeSuccess() {
        subj.start();
        assertNotNull(subj.getCurrentDeviceId());

        command();
    }

    @Test
    @Order(1)
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
