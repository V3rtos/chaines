package me.moonways.bridgenet.api.modern_command.argument;

@FunctionalInterface
public interface ArgumentValidator {

    boolean validate(String argument);
}
