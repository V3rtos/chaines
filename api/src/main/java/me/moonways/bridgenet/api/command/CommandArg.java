package me.moonways.bridgenet.api.command;

import java.lang.annotation.*;

@Repeatable(CommandHelper.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandArg {

    int position() default -1;

    CommandRegexId regexId() default @CommandRegexId;
}
