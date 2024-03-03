package me.moonways.bridgenet.api.modern_x2_command;

import me.moonways.bridgenet.api.modern_x2_command.entity.EntityCommandSender;

@InjectCommand()
public class TestCommand {

    @GeneralCommand("test")
    @CommandOperator('+')
    public void general(ExecutionContext executionContext) {
        EntityCommandSender entity = executionContext.getEntity();
        entity.sendMessage("Тест команды");
    }

    @SubCommand({"sub first", "sub one"})
    @Usage("Первая тестовая команда")
    @Cooldown(1000L)
    public void sub_test_first(ExecutionContext executionContext) {
        EntityCommandSender entity = executionContext.getEntity();
        entity.sendMessage("Тест первой кодкоманды");
    }

    @SubCommand({"sub seconds", "sub two"})
    @Usage("Вторая тестовая команда")
    public void sub_test_second(ExecutionContext executionContext) {
        EntityCommandSender entity = executionContext.getEntity();
        entity.sendMessage("Тест второй подкоманды");
    }

    @SubCommand("help")
    public void help(ExecutionContext executionContext) {
        EntityCommandSender entity = executionContext.getEntity();
        entity.sendMessage("Тест помощи");
    }
}
