package me.moonways.bridgenet.bootstrap.hook.console;

import me.moonways.bridgenet.api.command.CommandSession;
import me.moonways.bridgenet.api.command.EntityCommandSender;
import me.moonways.bridgenet.api.command.annotation.*;

@Command("user")
@Permission("test")
public class TestCommand {

    @Mentor
    public void defaultCommand(CommandSession session) {
        EntityCommandSender sender = session.getSender();

        sender.sendMessage("Дефолтное сообщение");
    }

    @Permission("test.info")
    @Producer(name = "info")
    public void handleInfo(CommandSession session) {
        EntityCommandSender sender = session.getSender();
    }

    @Matcher
    public boolean predicate_one(CommandSession session) {
        return true;
    }

   //@Predicate
   //public boolean predicate_two(CommandSession session) {
   //    EntityCommandSender sender = session.getSender();
   //    return true;
   //}
}
