package me.moonways.bridgenet;

import me.moonways.bridgenet.command.Command;
import me.moonways.bridgenet.command.CommandIdentifier;
import me.moonways.bridgenet.command.condition.CommandAccessDeniedType;
import me.moonways.bridgenet.command.condition.CommandCondition;
import me.moonways.bridgenet.command.sender.ConsoleSender;

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
