package me.moonways.bridgenet.test.api.command;

import me.moonways.bridgenet.api.command.CommandExecutionException;
import me.moonways.bridgenet.api.command.CommandExecutor;
import me.moonways.bridgenet.api.command.sender.ConsoleCommandSender;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
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
    public void test_success() {
        try {
            commandExecutor.execute(consoleCommandSender, "test");
        } catch (CommandExecutionException exception) {
            fail(exception.getMessage());
        }
    }
}
