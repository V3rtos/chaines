package me.moonways.bridgenet.api.modern_command.argument.type;

import me.moonways.bridgenet.api.modern_command.argument.SubcommandArgumentValidator;

public class SubcommandStringArgument implements SubcommandArgumentValidator {

    @Override
    public boolean validate(String argument) {
        return true;
    }
}
