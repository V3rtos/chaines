package me.moonways.bridgenet.test.engine.persistance;

import me.moonways.bridgenet.test.engine.component.module.Module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TestModules {

    Class<? extends Module>[] value();
}
