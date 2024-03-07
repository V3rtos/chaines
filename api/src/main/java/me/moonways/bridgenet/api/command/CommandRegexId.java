package me.moonways.bridgenet.api.command;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandRegexId {

    String value() default "";
}
