package me.moonways.bridgenet.api.event;

import me.moonways.bridgenet.api.inject.processor.persistence.UseTypeAnnotationProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@UseTypeAnnotationProcessor
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InboundEventListener {
}
