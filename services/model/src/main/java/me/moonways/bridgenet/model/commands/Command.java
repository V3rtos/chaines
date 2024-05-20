package me.moonways.bridgenet.model.commands;

import me.moonways.bridgenet.model.commands.arguments.ArgumentsContext;

public interface Command {

    CommandDescription getDescription();

    void prepare(ArgumentsContext argumentsContext);

    CommandResult run(CommandSessionContext context);
}
