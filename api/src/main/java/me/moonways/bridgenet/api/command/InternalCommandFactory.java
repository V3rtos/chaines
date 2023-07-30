package me.moonways.bridgenet.api.command;

import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.command.wrapper.WrappedArguments;
import me.moonways.bridgenet.api.command.wrapper.WrappedCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

class InternalCommandFactory {

    public CommandSession createSession(WrappedCommand wrappedCommand, EntityCommandSender sender, String[] args) {
        WrappedArguments arguments = createWrapper(args);
        return new CommandSession(wrappedCommand, sender, arguments);
    }

    public WrappedArguments createWrapper(@NotNull String[] args) {
        return new WrappedArguments(args);
    }

    public String findNameByLabel(@NotNull String label) {
        return label.split(" ")[0];
    }

    public String[] findArgumentsByLabel(@NotNull String label) {
        return label.split(" ");
    }

    public String[] copyArgumentsOfRange(@NotNull String[] arguments) {
        return Arrays.copyOfRange(arguments, 1, arguments.length);
    }
}
