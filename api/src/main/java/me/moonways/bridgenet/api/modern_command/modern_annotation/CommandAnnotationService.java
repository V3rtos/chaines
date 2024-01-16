package me.moonways.bridgenet.api.modern_command.modern_annotation;

import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
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
        injector.searchByProject(AutoregisterCommandAnnotation.class);

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

    public void processCommandAnnotations(@NotNull CommandSession session, @NotNull AnnotatedElement annotatedElement) {
        Set<Class<?>> registeredAnnotations = getRegisteredAnnotations();

        for (Class<?> registeredAnnotation : registeredAnnotations) {
            Annotation annotation = annotatedElement.getDeclaredAnnotation((Class<? extends Annotation>) registeredAnnotation);

            if (annotation != null) {
                CommandAnnotationContext<?> context = new CommandAnnotationContext<>(annotation, session, null); // todo
                processCommandAnnotation(context);
            }
        }
    }

    private <T extends Annotation> void processCommandAnnotation(CommandAnnotationContext<T> context) {
        CommandAnnotationProcessor<T> annotationHandler = (CommandAnnotationProcessor<T>)
                getAnnotationHandler(context.getAnnotation().annotationType());

        annotationHandler.updateCommandInfo(context);

        if (annotationHandler.verify(context)) {
            // todo - component of command processing...
        }
    }
}
