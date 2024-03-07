package me.moonways.bridgenet.api.command.api.process.verification.inject;

import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.IgnoredRegistry;
import me.moonways.bridgenet.api.command.api.process.verification.inject.validate.CommandAnnotationValidateRequest;
import me.moonways.bridgenet.api.command.api.process.verification.inject.validate.CommandAnnotationValidateResult;

import java.lang.annotation.Annotation;

@Getter
@ToString
@IgnoredRegistry
public abstract class CommandAnnotationHandler<T extends Annotation> {

    /**
     * Выполнение действия при аннотировании команды.
     * @param context - контекст аннотации.
     */
    public abstract void prepare(CommandBaseAnnotationContext<T> context);

    /**
     * Верифицируем команду относительно сессии команды на
     * возможность ее исполнения.
     *
     * @param request - запрос верификации.
     */
    public abstract CommandAnnotationValidateResult validate(CommandAnnotationValidateRequest<T> request);

}
