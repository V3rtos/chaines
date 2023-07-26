package me.moonways.bridgenet.api.intercept;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodPriority {

    int value();
}
