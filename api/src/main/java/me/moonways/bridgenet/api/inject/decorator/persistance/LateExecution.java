package me.moonways.bridgenet.api.inject.decorator.persistance;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LateExecution {

    long delay();

    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
