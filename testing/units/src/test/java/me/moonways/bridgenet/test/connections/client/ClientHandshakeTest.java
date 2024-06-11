package me.moonways.bridgenet.test.connections.client;

import me.moonways.bridgenet.api.command.CommandExecutor;
import me.moonways.bridgenet.api.command.exception.CommandExecutionException;
import me.moonways.bridgenet.api.command.sender.ConsoleCommandSender;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.message.Handshake;
import me.moonways.bridgenet.test.data.ExampleClient;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.component.module.impl.ClientModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import me.moonways.bridgenet.test.engine.persistance.TestOrdered;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(ModernTestEngineRunner.class)
@TestModules(ClientModule.class)
public class ClientHandshakeTest {

    @Inject
    private ExampleClient subj;

    @Inject
    private CommandExecutor commandExecutor;
    @Inject
    private ConsoleCommandSender consoleCommandSender;

    @Test
    @TestOrdered(1)
    public void test_handshakeSuccess() {
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
            commandExecutor.execute(consoleCommandSender, "servers get " + TestConst.Server.DESC.getName());
        } catch (CommandExecutionException exception) {
            fail(exception.getMessage());
        }
    }
}
