package me.moonways.bridgenet.api.modern_command.subcommand;

import me.moonways.bridgenet.api.modern_command.annotation.persistance.SubcommandArgument;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SubcommandArguments {

    SubcommandArgument[] value();
}
