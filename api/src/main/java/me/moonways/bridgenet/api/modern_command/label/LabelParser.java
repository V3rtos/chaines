package me.moonways.bridgenet.api.modern_command.label;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class LabelParser {

    public String getName(@NotNull String label) {
        return label.split(" ")[0];
    }

    public String[] getArguments(@NotNull String label) {
        String[] splitLabel = label.split(" ");

        return Arrays.copyOfRange(splitLabel, 1, splitLabel.length);
    }
}
