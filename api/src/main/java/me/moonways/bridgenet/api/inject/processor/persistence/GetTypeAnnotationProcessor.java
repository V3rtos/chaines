package me.moonways.bridgenet.api.inject.processor.persistence;

import java.lang.annotation.*;

/**
 * Данная аннотация возвращает результат
 * сканирования и процессинга аннотаций
 * в виде объекта TypeAnnotationProcessorResult
 * и автоматически инициализирует значение поля
 * при наличии аннотации @WaitTypeAnnotationProcessor
 * над классом, где применяется текущая аннотация.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GetTypeAnnotationProcessor {
}
