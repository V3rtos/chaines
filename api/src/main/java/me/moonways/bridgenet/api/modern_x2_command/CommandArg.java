package me.moonways.bridgenet.api.modern_x2_command;

import java.lang.annotation.*;

@Repeatable(CommandHelper.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandArg {

    int position();

    CommandRegexId regexId() default @CommandRegexId;
}
