package me.moonways.bridgenet.api.modern_command.argument.type;

import me.moonways.bridgenet.api.modern_command.argument.ArgumentValidator;

public class StringArgument implements ArgumentValidator {

    @Override
    public boolean validate(String argument) {
        return true;
    }
}
