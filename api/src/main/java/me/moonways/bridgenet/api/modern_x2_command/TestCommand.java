package me.moonways.bridgenet.api.modern_x2_command;

import me.moonways.bridgenet.api.modern_x2_command.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.result.CommandExecuteResult;
import me.moonways.bridgenet.api.modern_x2_command.result.CommandExecuteResultMsg;

@InjectCommand()
public class TestCommand {

    @GeneralCommand("test")
    public CommandExecuteResult general(ExecutionContext executionContext) {
        EntityCommandSender entity = executionContext.getEntity();
        return CommandExecuteResult.ok(entity, CommandExecuteResultMsg.SUCCESSFUL_DISPATCH);
    }

    @SubCommand({"sub first", "sub one"})
    @Usage("/sub first")
    @Description("команда для гиткодера")
    @Cooldown(1000L)
    public CommandExecuteResult sub_test_first(ExecutionContext executionContext) {
        EntityCommandSender entity = executionContext.getEntity();
        return CommandExecuteResult.fail(entity, "Failed dispatch command sub first");
    }

    @SubCommand({"sub seconds", "sub two"})
    @Usage("/sub second")
    @Description("команда для ликса")
    public CommandExecuteResult sub_test_second(ExecutionContext executionContext) {
        EntityCommandSender entity = executionContext.getEntity();
        return CommandExecuteResult.ok(entity, "Successful dispatch command sub second");
    }

    @SubCommand("help")
    public CommandExecuteResult help(ExecutionContext executionContext) {
        EntityCommandSender entity = executionContext.getEntity();
        return CommandExecuteResult.ok(entity, "Successful dispatch command help");
    }
}
