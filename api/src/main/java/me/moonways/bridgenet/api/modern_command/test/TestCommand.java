package me.moonways.bridgenet.api.modern_command.test;

import me.moonways.bridgenet.api.modern_command.*;
import me.moonways.bridgenet.api.modern_command.argument.type.StringArgument;
import me.moonways.bridgenet.api.modern_command.entity.ConsoleEntity;
import me.moonways.bridgenet.api.modern_command.entity.EntityType;
import me.moonways.bridgenet.api.modern_command.message.MessageBuilder;
import me.moonways.bridgenet.api.modern_command.modern_annotation.persistance.Description;
import me.moonways.bridgenet.api.modern_command.modern_annotation.persistance.EntityLevel;
import me.moonways.bridgenet.api.modern_command.modern_annotation.persistance.UsageCooldown;
import me.moonways.bridgenet.api.modern_command.modern_annotation.persistance.Permission;
import me.moonways.bridgenet.api.modern_command.session.CommandSession;
import me.moonways.bridgenet.api.modern_command.subcommand.SubcommandArgument;
import me.moonways.bridgenet.api.modern_command.subcommand.SubcommandUsageDescription;
import me.moonways.bridgenet.api.util.minecraft.ChatColor;

import java.util.concurrent.TimeUnit;

@Command
@Aliases("test")
@Permission("test.use")
@EntityLevel(EntityType.CONSOLE)
@UsageCooldown(time = 1, unit = TimeUnit.MINUTES)
public class TestCommand {

    @Permission("primary_command_test")
    @Aliases("primary_test_command")
    @Description("строка / булевое значение")
    @SubcommandUsageDescription("<string> <boolean>")
    @SubcommandArgument(argument = "{0}", argumentType = StringArgument.class)
    private void primary_subcommand(CommandSession session) {
        ConsoleEntity console = session.from(ConsoleEntity.class);

        console.sendMessage(MessageBuilder
                .newBuilder()
                .color(ChatColor.RED)
                .text("проверка")
                .color(ChatColor.WHITE)
                .text("команды")
                .build());

        session.block(TimeUnit.MINUTES.toMillis(1));
    }

    @Permission("secondary_command_test")
    @Aliases("secondary_test_command")
    @Description("строка / строка / число   ")
    @SubcommandUsageDescription("<string> <string> <integer>")
    @SubcommandArgument(argument = "{0}", argumentType = StringArgument.class)
    @SubcommandArgument(argument = "{1}", argumentType = StringArgument.class)
    @UsageCooldown(time = 1, unit = TimeUnit.MINUTES)
    private void secondary_subcommand() {
    }
}
