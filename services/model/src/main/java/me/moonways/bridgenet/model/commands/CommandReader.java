package me.moonways.bridgenet.model.commands;

import me.moonways.bridgenet.model.commands.arguments.ArgumentAdapter;
import me.moonways.bridgenet.model.commands.arguments.ArgumentsContext;

public interface CommandReader {

    String label();

    String readName();

    String[] readArguments();

    ArgumentAdapter readArgument();

    ArgumentsContext readArgumentsToContext();
}
