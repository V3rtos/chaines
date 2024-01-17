package me.moonways.bridgenet.api.modern_command.syntax;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_command.CommandContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SyntaxParser {

    @Inject
    private CommandContainer container;

    public String getCommandName(@NotNull String[] args) {
        return args[0];
    }

    public String[] getCommandArguments(int startCrop, String[] args) {
        return Arrays.copyOfRange(args, startCrop, args.length);
    }

    public String[] splitCommandLabelIntoArray(@NotNull String label) {
        return label.split(" ");
    }
}
