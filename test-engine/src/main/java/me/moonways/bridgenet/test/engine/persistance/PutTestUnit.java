package me.moonways.bridgenet.test.engine.persistance;

import me.moonways.bridgenet.api.inject.bean.factory.BeanFactoryProviders;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PutTestUnit {

    BeanFactoryProviders factory() default BeanFactoryProviders.CONSTRUCTOR;
}
