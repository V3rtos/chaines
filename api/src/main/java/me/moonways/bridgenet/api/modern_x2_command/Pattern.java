package me.moonways.bridgenet.api.modern_x2_command;

import me.moonways.bridgenet.api.modern_x2_command.obj.pattern.PatternFormat;

import java.lang.annotation.*;

@Repeatable(Patterns.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Pattern {

    PatternFormat enumFormat() default PatternFormat.EMPTY;

    String stringFormat() default "";

    String[] exceptionsMsg() default "Invalid pattern";
}
