package me.moonways.bridgenet.api.command.annotation;

import me.moonways.bridgenet.api.command.annotation.repeatable.RepeatableCommandOption;
import me.moonways.bridgenet.api.command.option.CommandParameterMatcher;

import java.lang.annotation.*;

@Repeatable(RepeatableCommandOption.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandParameter {

    Class<? extends CommandParameterMatcher> value();
}
