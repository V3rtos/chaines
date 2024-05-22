package me.moonways.bridgenet.test.connections.mtp;

import me.moonways.bridgenet.api.command.CommandExecutor;
import me.moonways.bridgenet.api.command.exception.CommandExecutionException;
import me.moonways.bridgenet.api.command.sender.ConsoleCommandSender;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.test.data.ExampleConnector;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.module.impl.AllModules;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import me.moonways.bridgenet.test.engine.persistance.TestOrdered;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(ModernTestEngineRunner.class)
@TestModules(AllModules.class)
public class ConnectorHandshakeTest {

    private final ExampleConnector subj = new ExampleConnector();

    @Inject
    private CommandExecutor commandExecutor;
    @Inject
    private ConsoleCommandSender consoleCommandSender;

    @Test
    @TestOrdered(1)
    public void test_handshakeSuccess() {
        subj.start();
        assertNotNull(subj.getCurrentDeviceId());

        command();
    }

    @Test
    @TestOrdered(2)
    public void test_handshakeFailed() {
        Handshake.Result result = subj.retryHandshakeExchanging();
        assertTrue(result instanceof Handshake.Failure);
    }

    private void command() {
        try {
            commandExecutor.execute(consoleCommandSender, "servers list");
            commandExecutor.execute(consoleCommandSender, "servers get " + TestConst.Connector.DEVICE_DESC.getName());
        } catch (CommandExecutionException exception) {
            fail(exception.getMessage());
        }
    }
}
