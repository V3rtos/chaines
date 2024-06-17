package me.moonways.bridgenet.test.engine.persistance;

import me.moonways.bridgenet.test.engine.component.step.Step;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BeforeSteps {

    Class<? extends Step>[] value();
}
