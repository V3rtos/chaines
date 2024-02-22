package me.moonways.bridgenet.api.modern_command.execution.annotation;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.modern_command.ExecutionContext;
import me.moonways.bridgenet.api.modern_command.execution.annotation.context.AnnotationContext;
import me.moonways.bridgenet.api.modern_command.Command;
import me.moonways.bridgenet.api.modern_command.execution.verify.CommandVerifyResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;

@Autobind
public class CommandAnnotationService {

    private final Map<Class<? extends Annotation>, CommandAnnotationHandler<?>> handlers = new HashMap<>();

    @Inject
    private DependencyInjection injector;

    @PostConstruct
    private void initHandlers() {
        //todo injector
    }

    private Set<Class<? extends Annotation>> getAnnotations() {
        return handlers.keySet();
    }

    public List<CommandVerifyResult> verifyAll(ExecutionContext<?> context) {
        List<CommandVerifyResult> results = new ArrayList<>();

        for (Class<? extends Annotation> annotationCls : getAnnotations()) {
            CommandVerifyResult result = verify(context, annotationCls);
        }
    }

    private <T extends Annotation> CommandVerifyResult verify(ExecutionContext<?> context, AnnotatedElement annotatedElement, Class<T> cls) {
        CommandAnnotationHandler<T> annotationHandler = getHandler(cls);

        T annotation = annotatedElement.getDeclaredAnnotation(cls);
        AnnotationContext<T> annotationContext = new AnnotationContext<>(annotation, command);

        return annotationHandler.verify(annotationContext, session);
    }

    @SuppressWarnings("unchecked")
    private <T extends Annotation> CommandAnnotationHandler<T> getHandler(Class<? extends Annotation> cls) {
        return (CommandAnnotationHandler<T>) handlers.get(cls);
    }
}
