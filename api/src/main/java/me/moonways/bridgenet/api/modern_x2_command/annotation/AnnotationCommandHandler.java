package me.moonways.bridgenet.api.modern_x2_command.annotation;

import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.modern_x2_command.annotation.validate.AnnotationCommandValidateRequest;
import me.moonways.bridgenet.api.modern_x2_command.annotation.validate.AnnotationCommandValidateResult;

import java.lang.annotation.Annotation;

@Getter
@ToString
public abstract class AnnotationCommandHandler<T extends Annotation> {

    /**
     * Выполнение действия при аннотировании команды.
     * @param context - контекст аннотации.
     */
    public abstract void prepare(AnnotationCommandContext<T> context);

    /**
     * Верифицируем команду относительно сессии команды на
     * возможность ее исполнения.
     *
     * @param request - запрос верификации.
     */
    public abstract AnnotationCommandValidateResult validate(AnnotationCommandValidateRequest<T> request);

}
