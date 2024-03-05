package me.moonways.bridgenet.api.modern_x2_command;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandRegexId {

    String value() default "";
}
