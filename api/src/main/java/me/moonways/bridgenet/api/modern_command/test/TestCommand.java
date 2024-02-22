package me.moonways.bridgenet.api.modern_command.test;

import me.moonways.bridgenet.api.modern_command.*;
import me.moonways.bridgenet.api.modern_command.depend.*;
import me.moonways.bridgenet.api.modern_x2_command.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.entity.EntitySenderType;

import java.util.concurrent.TimeUnit;

@InjectCommand
public class TestCommand {

    @Parent
    @Aliases("test")
    @Permission("test.use")
    @Entity(EntitySenderType.CONSOLE)
    @Cooldown(time = 1, unit = TimeUnit.MINUTES)
    public void parent(ExecutionContext<EntityCommandSender> context) {
    }

    @Help
    public void help(ExecutionContext<EntityCommandSender> context) {
    }

    @Permission("primary")
    @Aliases("primary")
    @Description("строка / булевое значение")
    @Usage("<string> <boolean>")
    @Pattern(position = 0, value = "^(?=.+[a-z])", exception = "Команда должна содержать символы")
    @Cooldown(time = 1, unit = TimeUnit.MINUTES)
    @Entity(EntitySenderType.USER)
    public void primary_subcommand(ExecutionContext<EntityCommandSender> context) {
        EntityCommandSender console = context.getEntity();
        console.sendMessage("test command");
    }

    @Permission("secondary")
    @Aliases("secondary")
    @Description("строка / строка / число   ")
    @Usage("<string> <string> <integer>")
    @Cooldown(time = 1, unit = TimeUnit.MINUTES)
    public void secondary_subcommand(ExecutionContext<EntityCommandSender> context) {
    }
}
