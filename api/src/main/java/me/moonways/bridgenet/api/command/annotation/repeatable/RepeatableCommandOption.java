package me.moonways.bridgenet.api.command.annotation.repeatable;

import me.moonways.bridgenet.api.command.annotation.CommandOption;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RepeatableCommandOption {

    CommandOption[] value();
}
