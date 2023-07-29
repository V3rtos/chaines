package me.moonways.service.api.command;

import me.moonways.service.api.command.annotation.*;

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

        if (!sender.hasPermission("test")) {
            sender.sendMessage("У вас нет прав!");
            return;
        }

        ArgumentArrayWrapper wrapper = session.getArgumentWrapper();
    }

    @Predicate
    public boolean predicate_one(CommandSession session) {
        return true;
    }

   //@Predicate
   //public boolean predicate_two(CommandSession session) {
   //    EntityCommandSender sender = session.getSender();
   //    return true;
   //}
}
