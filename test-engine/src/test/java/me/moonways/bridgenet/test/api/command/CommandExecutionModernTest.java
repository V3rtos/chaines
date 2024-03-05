package me.moonways.bridgenet.test.api.command;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_x2_command.process.service.CommandService;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(BridgenetJUnitTestRunner.class)
public class CommandExecutionModernTest {

    @Inject
    private CommandService commandService;

    @Test
    public void test_dispatchSuccess() {
        commandService.dispatchConsole("test info info info член GitCoder");
    }

    @Test
    public void test_dispatchNotEnoughArguments() {
        commandService.dispatchConsole("test info info info член");
    }
}
