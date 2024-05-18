package me.moonways.bridgenet.api.modern_command.test;

import me.moonways.bridgenet.api.modern_command.CommandElementType;
import me.moonways.bridgenet.api.modern_command.object.ExecutionContext;
import me.moonways.bridgenet.api.modern_command.persistance.*;

@NamedCommand("test")
public class TestCommand {

    @CommandElement(CommandElementType.EXTERNAL)
    public void test(ExecutionContext context) {

    }

    @Alias({"gitcoder", "stonlex"})
    @Description("Выполнение первой подкоманды")
    @Usage("/test gitcoder")
    @CommandElement(CommandElementType.INTERNAL)
    public void sub_test(ExecutionContext context) {

    }
}
