package me.moonways.bridgenet.api.modern_command.modern_annotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.lang.annotation.Annotation;

@ToString
@RequiredArgsConstructor
public abstract class CommandAnnotationProcessor<T extends Annotation> {

    @Getter
    private final Class<T> annotationType;

    /**
     * Регистрация аннотации как внутреннего параметра команды.
     * @param context - контекст аннотации.
     */
    protected abstract void updateCommandInfo(CommandAnnotationContext<T> context);

    /**
     * Верифицируем команду относительно сессии команды на
     * возможность ее исполнения.
     *
     * @param context - контекст аннотации.
     */
    protected boolean verify(CommandAnnotationContext<T> context) {
        // override me.
        return true;
    }
}
