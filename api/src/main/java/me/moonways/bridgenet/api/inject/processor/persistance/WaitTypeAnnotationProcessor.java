package me.moonways.bridgenet.api.inject.processor.persistance;

import java.lang.annotation.*;

/**
 * Данная аннотация ставит в ожидание бин,
 * на который она была повешена, до инициализации
 * аннотированных бинов, помеченных указанным
 * типом аннотации в значении текущей.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WaitTypeAnnotationProcessor {

    Class<? extends Annotation>[] value();
}
