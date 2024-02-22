package me.moonways.bridgenet.api.modern_x2_command;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Sub {

    String[] value();
}
