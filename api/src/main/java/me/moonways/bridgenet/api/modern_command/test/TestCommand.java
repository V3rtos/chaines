package me.moonways.bridgenet.api.modern_command.test;

import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.modern_command.*;
import me.moonways.bridgenet.api.modern_command.argument.StringArgument;
import me.moonways.bridgenet.api.modern_command.entity.ConsoleCommandSender;
import me.moonways.bridgenet.api.modern_command.entity.EntityType;
import me.moonways.bridgenet.api.modern_command.message.MessageBuilder;
import me.moonways.bridgenet.api.modern_command.annotation.persistance.Description;
import me.moonways.bridgenet.api.modern_command.annotation.persistance.EntityLevel;
import me.moonways.bridgenet.api.modern_command.annotation.persistance.Cooldown;
import me.moonways.bridgenet.api.modern_command.annotation.persistance.Permission;
import me.moonways.bridgenet.api.modern_command.session.CommandSession;
import me.moonways.bridgenet.api.modern_command.annotation.persistance.SubcommandArgument;
import me.moonways.bridgenet.api.modern_command.subcommand.SubcommandUsage;
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
