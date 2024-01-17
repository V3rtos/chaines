package me.moonways.bridgenet.api.modern_command.argument;

public class BooleanArgument implements ArgumentValidator {

    @Override
    public boolean validate(String argument) {
        return Boolean.parseBoolean(argument);
    }
}
