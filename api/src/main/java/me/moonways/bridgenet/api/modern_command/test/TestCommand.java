package me.moonways.bridgenet.api.modern_command.test;

import me.moonways.bridgenet.api.modern_command.*;
import me.moonways.bridgenet.api.modern_command.entity.ConsoleCommandSender;
import me.moonways.bridgenet.api.modern_command.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_command.entity.EntityType;
import me.moonways.bridgenet.api.modern_command.session.CommandSession;

import java.util.concurrent.TimeUnit;

@InjectCommand
public class TestCommand {

    @Parent
    @Aliases("test")
    @Permission("test.use")
    @Entity(EntityType.CONSOLE)
    @Cooldown(time = 1, unit = TimeUnit.MINUTES)
    public void parent(CommandSession session) {
    }

    @Help
    public void help(CommandSession session) {
    }

    @Permission("primary")
    @Aliases("primary")
    @Description("строка / булевое значение")
    @Usage("<string> <boolean>")
    @Pattern(position = 0, value = "^(?=.+[a-z])", exception = "Команда должна содержать символы")
    @Cooldown(time = 1, unit = TimeUnit.MINUTES)
    @Entity(EntityType.USER)
    public void primary_subcommand(CommandSession session) {
        EntityCommandSender console = session.from(ConsoleCommandSender.class);
        console.sendMessage("привет я гитдрочилдрен");

        session.block(1, TimeUnit.MINUTES);
    }

    @Permission("secondary")
    @Aliases("secondary")
    @Description("строка / строка / число   ")
    @Usage("<string> <string> <integer>")
    @Cooldown(time = 1, unit = TimeUnit.MINUTES)
    public void secondary_subcommand(CommandSession session) {
    }
}
