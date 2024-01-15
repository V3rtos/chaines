package me.moonways.bridgenet.test.api.command;

import me.moonways.bridgenet.api.command.CommandSession;
import me.moonways.bridgenet.api.command.option.OnlyConsoleSenderOption;
import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.command.annotation.*;

@Command("test")
@Permission("test")
@CommandOption(OnlyConsoleSenderOption.class)
public class TestCommand {

    @MentorExecutor
    public void defaultCommand(CommandSession session) {
        EntityCommandSender sender = session.getSender();
        sender.sendMessage("Дефолтное сообщение");
    }

    @Permission("test.info")
    @ProducerExecutor("info")
    public void handleInfo(CommandSession session) {
        EntityCommandSender sender = session.getSender();
    }

    @MatcherExecutor
    public boolean predicate_one(CommandSession session) {
        return true;
    }
}
