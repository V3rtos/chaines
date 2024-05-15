package me.moonways.bridgenet.test.engine.persistance;

import me.moonways.bridgenet.test.engine.module.TestEngineModule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TestModules {

    Class<? extends TestEngineModule>[] value();
}
