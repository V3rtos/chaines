package me.moonways.bridgenet.api.command.annotation.repeatable;

import me.moonways.bridgenet.api.command.annotation.CommandParameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RepeatableCommandOption {

    CommandParameter[] value();
}
