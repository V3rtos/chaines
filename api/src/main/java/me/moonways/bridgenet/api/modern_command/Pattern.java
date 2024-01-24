package me.moonways.bridgenet.api.modern_command;

import java.lang.annotation.*;

@Repeatable(Patterns.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Pattern {

    int position();

    String value();

    String exception() default "Invalid pattern";
}
