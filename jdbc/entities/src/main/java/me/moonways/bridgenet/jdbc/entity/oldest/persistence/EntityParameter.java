package me.moonways.bridgenet.jdbc.entity.persistence;

import me.moonways.bridgenet.jdbc.core.compose.ParameterSignature;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EntityParameter {

    int order() default 0;

    String id() default "";

    ParameterSignature[] indexes() default { };
}
