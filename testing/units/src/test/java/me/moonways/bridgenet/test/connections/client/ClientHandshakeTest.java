package me.moonways.bridgenet.test.connections.client;

import me.moonways.bridgenet.api.command.CommandExecutor;
import me.moonways.bridgenet.api.command.exception.CommandExecutionException;
import me.moonways.bridgenet.api.command.sender.ConsoleCommandSender;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.message.Handshake;
import me.moonways.bridgenet.test.data.ExampleClient;
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
public class ClientHandshakeTest {

    private final ExampleClient subj = new ExampleClient();

    @Inject
    private CommandExecutor commandExecutor;
    @Inject
    private ConsoleCommandSender consoleCommandSender;

    @Test
    @TestOrdered(1)
    public void test_handshakeSuccess() {
        subj.start();
        assertNotNull(subj.getCurrentClientId());

        testServersCommand();
    }

    @Test
    @TestOrdered(2)
    public void test_handshakeFailed() {
        Handshake.Result result = subj.retryHandshakeExchanging();
        assertTrue(result instanceof Handshake.Failure);
    }

    private void testServersCommand() {
        try {
            commandExecutor.execute(consoleCommandSender, "servers list");
            commandExecutor.execute(consoleCommandSender, "servers get " + TestConst.Connector.CLIENT_INFO.getName());
        } catch (CommandExecutionException exception) {
            fail(exception.getMessage());
        }
    }
}
