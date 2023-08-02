package me.moonways.bridgenet.api.command;

import me.moonways.bridgenet.api.command.children.CommandChild;
import me.moonways.bridgenet.api.command.option.CommandOptionMatcher;
import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.command.wrapper.WrappedArguments;
import me.moonways.bridgenet.api.command.wrapper.WrappedCommand;

import java.util.Arrays;
import java.util.List;

class InternalCommandFactory {

    public CommandSession createSession(CommandSession.HelpMessageView helpMessageView, EntityCommandSender sender, String[] args) {
        final WrappedArguments arguments = createArgumentsWrapper(args);
        return new CommandSession(helpMessageView, sender, arguments);
    }

    public WrappedArguments createArgumentsWrapper(String[] args) {
        return new WrappedArguments(args);
    }

    public WrappedCommand createCommandWrapper(Object source, String name, String permission,
                                               List<CommandChild> childrenList,
                                               List<CommandOptionMatcher> optionsList,
                                               CommandSession.HelpMessageView helpMessageView) {
        return new WrappedCommand(name, permission, source, childrenList, optionsList, helpMessageView);
    }

    public String findNameByLabel(String label) {
        return label.split(" ")[0];
    }

    public String[] findArgumentsByLabel(String label) {
        return label.split(" ");
    }

    public String[] copyArgumentsOfRange(String[] arguments) {
        return Arrays.copyOfRange(arguments, 1, arguments.length);
    }
}
