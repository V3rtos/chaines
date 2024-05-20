package me.moonways.bridgenet.model.commands;

import me.moonways.bridgenet.model.commands.arguments.ArgumentsContext;

public interface CommandSessionContext {

    //EntityAudience getEntity();

    ArgumentsContext getArguments();

    String getLabel();
}
