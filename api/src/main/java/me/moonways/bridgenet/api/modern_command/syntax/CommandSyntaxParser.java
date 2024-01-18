package me.moonways.bridgenet.api.modern_command.syntax;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CommandSyntaxParser {

    public String getName(@NotNull String[] args) {
        return args[0];
    }

    public String[] getArguments(int startCrop, String[] args) {
        return Arrays.copyOfRange(args, startCrop, args.length);
    }

    public String[] splitLabel(@NotNull String label) {
        return label.split(" ");
    }
}
