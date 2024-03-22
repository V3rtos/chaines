package me.moonways.bridgenet.jdbc.entity.persistence;

import me.moonways.bridgenet.jdbc.core.compose.ParameterAddon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EntityId {

    String id() default "id";

    ParameterAddon[] indexes() default {ParameterAddon.INCREMENTING, ParameterAddon.NOTNULL, ParameterAddon.PRIMARY};
}
