package me.moonways.bridgenet.rest.client.repository.persistence.param;

import me.moonways.bridgenet.rest.api.exchange.entity.ExchangeableEntity;
import me.moonways.bridgenet.rest.api.exchange.entity.type.TextEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestEntity {

    Class<? extends ExchangeableEntity> value() default TextEntity.class;
}
