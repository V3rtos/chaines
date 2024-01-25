package me.moonways.bridgenet.api.modern_command.annotation;

import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.modern_command.annotation.context.AnnotationContext;
import me.moonways.bridgenet.api.modern_command.annotation.context.SessionAnnotationContext;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

@Getter
@ToString
public abstract class AbstractCommandAnnotationHandler<T extends Annotation> {

    private final Class<T> annotationType;

    public AbstractCommandAnnotationHandler(Class<T> annotationType) {
        this.annotationType = annotationType;
    }

    /**
     * Регистрация аннотации как внутреннего параметра команды.
     * @param context - контекст аннотации.
     */
    protected void modify(AnnotationContext<T> context) {
        // override me.
    }

    /**
     * Выполнение действия перед выполнением команды.
     * @param context - контекст аннотации.
     */
    protected void handle(SessionAnnotationContext<T> context) {
        // override me.
    }

    /**
     * Верифицируем команду относительно сессии команды на
     * возможность ее исполнения.
     *
     * @param context - контекст аннотации.
     */
    protected Result verify(SessionAnnotationContext<T> context) {
        // override me.
        return Result.success();
    }


    public static class Result {

        private final boolean value;
        @Getter
        private final String message;

        public boolean isSuccess() {
            return value;
        }

        public boolean isFailed() {
            return !value;
        }

        private Result(boolean value, String message) {
            this.value = value;
            this.message = message;
        }

        public static Result success(@Nullable String message) {
            return new Result(true, message);
        }

        public static Result success() {
            return new Result(true, null);
        }

        public static Result error(String message) {
            return new Result(false, message);
        }
    }
}
