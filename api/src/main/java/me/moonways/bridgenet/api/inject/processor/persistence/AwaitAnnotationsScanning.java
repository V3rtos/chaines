package me.moonways.bridgenet.api.inject.processor.persistence;

import java.lang.annotation.*;

/**
 * Данная аннотация ставит в ожидание бин,
 * на который она была повешена, до инициализации
 * аннотированных бинов, помеченных указанным
 * типом аннотации в значении.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AwaitAnnotationsScanning {

    Class<? extends Annotation>[] value();
}
