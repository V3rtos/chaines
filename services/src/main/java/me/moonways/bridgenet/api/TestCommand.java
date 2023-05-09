package me.moonways.bridgenet.api;

import me.moonways.bridgenet.api.command.Command;
import me.moonways.bridgenet.api.command.CommandIdentifier;
import me.moonways.bridgenet.api.command.condition.CommandAccessDeniedType;
import me.moonways.bridgenet.api.command.condition.CommandCondition;
import me.moonways.bridgenet.api.command.sender.ConsoleSender;

@CommandIdentifier(name = "moonways")
public class TestCommand extends Command {

    @Override
    protected void register() {
        CommandCondition commandCondition = new CommandCondition(this);
        commandCondition.addCondition(CommandAccessDeniedType.DO_NOT_HAVE_PERMISSION,
                sender -> sender.hasPermission("moonways"));

        addSubcommand("test", commandCondition, (args, session) -> {
                    ConsoleSender consoleSender = session.senderCast(ConsoleSender.class);

                    consoleSender.sendMessage("Здарова зайбал!");
                });
    }
}
