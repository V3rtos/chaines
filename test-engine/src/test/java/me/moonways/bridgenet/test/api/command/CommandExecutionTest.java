package me.moonways.bridgenet.test.api.command;

import me.moonways.bridgenet.api.command.exception.CommandExecutionException;
import me.moonways.bridgenet.api.command.CommandExecutor;
import me.moonways.bridgenet.api.command.sender.ConsoleCommandSender;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import me.moonways.bridgenet.test.engine.persistance.Order;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;

@RunWith(BridgenetJUnitTestRunner.class)
public class CommandExecutionTest {

    @Inject
    private CommandExecutor commandExecutor;
    @Inject
    private ConsoleCommandSender consoleCommandSender;

    @Test
    @Order(0) //todo
    public void test_executeMentor() {
        try {
            commandExecutor.execute(consoleCommandSender, "test");
        } catch (CommandExecutionException exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    @Order(1)
    public void test_executeProducerWithoutArguments() {
        try {
            commandExecutor.execute(consoleCommandSender, "test info");
            commandExecutor.execute(consoleCommandSender, "test get");
            commandExecutor.execute(consoleCommandSender, "test player");
        } catch (CommandExecutionException exception) {
            fail(exception.getMessage());
        }
    }
}
