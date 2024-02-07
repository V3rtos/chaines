package me.moonways.bridgenet.api.util.autorun.persistance;

import me.moonways.bridgenet.api.inject.processor.persistance.UseTypeAnnotationProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@UseTypeAnnotationProcessor
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoRunner {
}
