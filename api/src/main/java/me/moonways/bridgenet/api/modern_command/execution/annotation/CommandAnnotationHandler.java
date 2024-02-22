package me.moonways.bridgenet.api.modern_command.execution.annotation;

import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.modern_command.execution.annotation.context.AnnotationContext;
import me.moonways.bridgenet.api.modern_command.execution.verify.CommandVerifyResult;

import java.lang.annotation.Annotation;

@Getter
@ToString
public abstract class CommandAnnotationHandler<T extends Annotation> {

    private final Class<T> annotationType;

    public CommandAnnotationHandler(Class<T> annotationType) {
        this.annotationType = annotationType;
    }

    /**
     * Регистрация аннотации как внутреннего параметра команды.
     * @param context - контекст аннотации.
     */
    public abstract void modify(AnnotationContext<T> context);

    /**
     * Верифицируем команду относительно сессии команды на
     * возможность ее исполнения.
     *
     * @param context - контекст аннотации.
     * @param session - сессия игрока.
     */
    public abstract CommandVerifyResult verify(AnnotationContext<T> context, CommandSession session);
}
