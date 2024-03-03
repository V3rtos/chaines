package me.moonways.bridgenet.api.modern_x2_command;

import me.moonways.bridgenet.api.modern_x2_command.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Patterns {

    Pattern[] value();
}
