package me.moonways.bridgenet.api.inject.decorator.definition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequiredNotNull {

    String message() default "null";

    boolean printStackTrace() default true;
}
