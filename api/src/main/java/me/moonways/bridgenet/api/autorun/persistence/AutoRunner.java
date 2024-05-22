package me.moonways.bridgenet.api.autorun.persistence;

import me.moonways.bridgenet.api.inject.processor.persistence.UseTypeAnnotationProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@UseTypeAnnotationProcessor
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoRunner {
}
