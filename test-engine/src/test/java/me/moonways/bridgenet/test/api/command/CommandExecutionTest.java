package me.moonways.bridgenet.test.api.command;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.command.api.uses.CommandExecutionContext;
import me.moonways.bridgenet.api.command.api.label.CommandLabelContext;
import me.moonways.bridgenet.api.command.api.process.CommandService;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(BridgenetJUnitTestRunner.class)
public class CommandExecutionTest {

    @Inject
    private CommandService commandService;

    @Test
    public void test_dispatchSuccess() {
        Optional<CommandExecutionContext> execution = commandService.dispatchConsole("test info GitCoder");

        assertTrue(execution.isPresent());

        CommandLabelContext.Arguments arguments = execution.get().getArguments();

        assertEquals(1, arguments.size());
        assertEquals("GitCoder", arguments.first().get());
    }

    @Test
    public void test_dispatchNotEnoughArguments() {
        Optional<CommandExecutionContext> execution = commandService.dispatchConsole("test info");

        assertTrue(execution.isPresent());

        CommandLabelContext.Arguments arguments = execution.get().getArguments();

        assertEquals(0, arguments.size());
    }
}
