package me.moonways.bridgenet.api.modern_command.annotation;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.modern_command.Aliases;
import me.moonways.bridgenet.api.modern_command.session.CommandSession;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Autobind
@SuppressWarnings("unchecked")
public class CommandAnnotationService {

    private final Map<Class<?>, CommandAnnotationProcessor<?>> annotationProcessorsMap = new HashMap<>();

    @Inject
    private DependencyInjection injector;

    @PostConstruct
    private void initAnnotationProcessors() {
        injector.peekAnnotatedMembers(AutoregisterCommandAnnotation.class);

        annotationProcessorsMap.putAll(injector.getContainer().getStoredInstances(AutoregisterCommandAnnotation.class)
                .stream()
                .filter(resource -> resource instanceof CommandAnnotationProcessor)
                .map(resource -> (CommandAnnotationProcessor<?>) resource)
                .collect(Collectors.toMap(
                        CommandAnnotationProcessor::getAnnotationType,
                        processor -> processor
                )));
    }

    public Set<Class<?>> getRegisteredAnnotations() {
        return Collections.unmodifiableSet(annotationProcessorsMap.keySet());
    }

    public <V extends Annotation> CommandAnnotationProcessor<V> getAnnotationHandler(Class<V> annotationType) {
        return (CommandAnnotationProcessor<V>) annotationProcessorsMap.get(annotationType);
    }

    public boolean isSubcommand(Method method) {
        return method.isAnnotationPresent(Aliases.class);
    }

    public boolean processCommandAnnotations(@NotNull CommandSession session, @NotNull AnnotatedElement annotatedElement) {
        Set<Class<?>> registeredAnnotations = getRegisteredAnnotations();

        for (Class<?> registeredAnnotation : registeredAnnotations) {
            if (applyCommandAnnotation(session, annotatedElement, (Class<? extends Annotation>) registeredAnnotation)) {
                return true;
            }
        }

        return false;
    }

    private <T extends Annotation> boolean applyCommandAnnotation(CommandSession session, AnnotatedElement annotatedElement, Class<T> annotationType) {
        T annotation = annotatedElement.getDeclaredAnnotation(annotationType);

        if (annotation != null) {
            CommandAnnotationContext<T> context = new CommandAnnotationContext<>(annotation, session, null);
            return processCommandAnnotation(context);
        }

        return false;
    }

    private <T extends Annotation> boolean processCommandAnnotation(CommandAnnotationContext<T> context) {
        CommandAnnotationProcessor<T> annotationHandler = (CommandAnnotationProcessor<T>)
                getAnnotationHandler(context.getAnnotation().annotationType());

        annotationHandler.updateCommandInfo(context);

        return annotationHandler.verify(context);
    }
}
