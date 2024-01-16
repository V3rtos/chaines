package me.moonways.bridgenet.api.command.annotation;


import me.moonways.bridgenet.api.command.annotation.repeatable.RepeatableCommandAliases;
import me.moonways.bridgenet.api.command.annotation.repeatable.RepeatableCommandOption;

import java.lang.annotation.*;

@Repeatable(RepeatableCommandAliases.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Alias {

    String value();
}
