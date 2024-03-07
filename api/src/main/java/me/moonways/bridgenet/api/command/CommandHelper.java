package me.moonways.bridgenet.api.command;

import me.moonways.bridgenet.api.command.api.uses.entity.EntitySenderType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CommandHelper {

    CommandArg[] value() default @CommandArg;

    EntitySenderType[] senderType() default {EntitySenderType.USER, EntitySenderType.CONSOLE};

    String usage() default "";

    String description() default "";
}
