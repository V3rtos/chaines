package me.moonways.bridgenet.test.engine.persistance;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SleepExecution {

    long duration();

    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
