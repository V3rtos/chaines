package me.moonways.bridgenet.api.proxy;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodPriority {

    int value();
}
