package me.moonways.bridgenet.api.modern_command.argument;

public class IntegerArgument implements ArgumentValidator {

    @Override
    public boolean validate(String argument) {
        return argument.matches("[0-9]+");
    }
}
