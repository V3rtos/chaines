package me.moonways.bridgenet.rest.client.repository.markers.param;

import me.moonways.rest.api.exchange.entity.ExchangeableEntity;
import me.moonways.rest.api.exchange.entity.type.TextEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestEntity {

    Class<? extends ExchangeableEntity> value() default TextEntity.class;
}
