package me.moonways.bridgenet.api.modern_command.test;

import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.modern_command.annotation.value.*;
import me.moonways.bridgenet.api.modern_command.annotation.value.repeatable.SubcommandArgument;
import me.moonways.bridgenet.api.modern_command.argument.StringArgument;
import me.moonways.bridgenet.api.modern_command.entity.ConsoleCommandSender;
import me.moonways.bridgenet.api.modern_command.entity.EntityType;
import me.moonways.bridgenet.api.modern_command.message.MessageBuilder;
import me.moonways.bridgenet.api.modern_command.session.CommandSession;
import me.moonways.bridgenet.api.modern_command.annotation.value.SubcommandUsage;
import me.moonways.bridgenet.api.util.minecraft.ChatColor;

import java.util.concurrent.TimeUnit;

@Command
@Aliases("test")
@Permission("test.use")
@EntityLevel(EntityType.CONSOLE)
@Cooldown(time = 1, unit = TimeUnit.MINUTES)
public class TestCommand {

    @Parent
    public void parent(CommandSession session) {
    }

    @Help
    public void help(CommandSession session) {
    }

    @Permission("primary")
    @Aliases("primary")
    @Description("строка / булевое значение")
    @SubcommandUsage("<string> <boolean>")
    @SubcommandArgument(argument = "{0}", argumentType = StringArgument.class)
    public void primary_subcommand(CommandSession session) {
        EntityCommandSender console = session.from(ConsoleCommandSender.class);

        console.sendMessage(MessageBuilder
                .newBuilder()
                .color(ChatColor.RED)
                .text("проверка")
                .color(ChatColor.WHITE)
                .text("команды")
                .build());

        session.block(TimeUnit.MINUTES.toMillis(1));
    }

    @Permission("secondary")
    @Aliases("secondary")
    @Description("строка / строка / число   ")
    @SubcommandUsage("<string> <string> <integer>")
    @SubcommandArgument(argument = "{0}", argumentType = StringArgument.class)
    @SubcommandArgument(argument = "{1}", argumentType = StringArgument.class)
    @Cooldown(time = 1, unit = TimeUnit.MINUTES)
    public void secondary_subcommand(CommandSession session) {
    }
}
