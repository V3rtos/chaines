package me.moonways.bridgenet.api.modern_command;

import me.moonways.bridgenet.api.modern_command.argument.SubcommandArgumentValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SubcommandArgument {

    String arguments();

    Class<? extends SubcommandArgumentValidator>[] argumentsType();
}
