package me.moonways.bridgenet.api.modern_x2_command.ai;

import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.modern_x2_command.ai.validate.AICommandValidateRequest;
import me.moonways.bridgenet.api.modern_x2_command.ai.validate.AICommandValidateResult;

import java.lang.annotation.Annotation;

@Getter
@ToString
public abstract class AICommandHandler<T extends Annotation> {

    private final Class<T> annotationType;

    public AICommandHandler(Class<T> annotationType) {
        this.annotationType = annotationType;
    }

    /**
     * Выполнение действия при аннотировании команды.
     * @param context - контекст аннотации.
     */
    public abstract void load(AICommandContext<T> context);

    /**
     * Верифицируем команду относительно сессии команды на
     * возможность ее исполнения.
     *
     * @param request - запрос верификации.
     */
    public abstract AICommandValidateResult validate(AICommandValidateRequest<T> request);
}
