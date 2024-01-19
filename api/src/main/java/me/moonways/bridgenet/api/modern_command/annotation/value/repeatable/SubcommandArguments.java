package me.moonways.bridgenet.api.modern_command.annotation.value.repeatable;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SubcommandArguments {

    SubcommandArgument[] value();
}
