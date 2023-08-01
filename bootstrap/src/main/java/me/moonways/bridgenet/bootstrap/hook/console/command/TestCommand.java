package me.moonways.bridgenet.bootstrap.hook.console.command;

import me.moonways.bridgenet.api.command.CommandSession;
import me.moonways.bridgenet.api.command.option.OnlyConsoleSenderOption;
import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.command.annotation.*;

@Command("user")
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

   //@Predicate
   //public boolean predicate_two(CommandSession session) {
   //    EntityCommandSender sender = session.getSender();
   //    return true;
   //}
}
