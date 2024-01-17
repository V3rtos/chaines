package me.moonways.bridgenet.api.modern_command.annotation.persistance;

import me.moonways.bridgenet.api.modern_command.argument.ArgumentValidator;
import me.moonways.bridgenet.api.modern_command.subcommand.SubcommandArguments;

import java.lang.annotation.*;

@Repeatable(SubcommandArguments.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubcommandArgument {

    String argument();

    Class<? extends ArgumentValidator> argumentType();
}
