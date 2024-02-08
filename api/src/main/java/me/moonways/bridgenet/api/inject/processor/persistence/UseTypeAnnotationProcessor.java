package me.moonways.bridgenet.api.inject.processor.persistence;

import java.lang.annotation.*;

/**
 * Данная аннотация помечается на аннотации,
 * которым требуется автоматизация процессинга
 * аннотаций. Для них будет создаваться стандартный
 * процессинг аннотаций, который по стандарту
 * будет кешировать найденные бины и помечать как
 * забинденные.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UseTypeAnnotationProcessor {
}
