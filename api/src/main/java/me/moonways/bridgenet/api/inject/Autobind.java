package me.moonways.bridgenet.api.inject;

import me.moonways.bridgenet.api.inject.bean.factory.BeanFactoryProviders;
import me.moonways.bridgenet.api.inject.processor.persistence.UseTypeAnnotationProcessor;

import java.lang.annotation.*;

@UseTypeAnnotationProcessor
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Autobind {

    BeanFactoryProviders provider() default BeanFactoryProviders.CONSTRUCTOR;
}
