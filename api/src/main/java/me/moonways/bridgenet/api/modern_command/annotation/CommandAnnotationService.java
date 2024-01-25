package me.moonways.bridgenet.api.modern_command.annotation;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.modern_command.CommandAnnotationHandler;
import me.moonways.bridgenet.api.modern_command.annotation.context.AnnotationContext;
import me.moonways.bridgenet.api.modern_command.annotation.context.SessionAnnotationContext;
import me.moonways.bridgenet.api.modern_command.data.CommandInfo;
import me.moonways.bridgenet.api.modern_command.session.CommandSession;
import me.moonways.bridgenet.api.modern_command.util.CommandReflectionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Autobind
public class CommandAnnotationService {

    private final Map<Class<? extends Annotation>, AbstractCommandAnnotationHandler<?>> annotationProcessorsMap = new HashMap<>();

    @Inject
    private DependencyInjection injector;

    @PostConstruct
    private void initAnnotationProcessors() {
        injector.peekAnnotatedMembers(CommandAnnotationHandler.class);

        annotationProcessorsMap.putAll(injector.getContainer().getStoredInstances(CommandAnnotationHandler.class)
                .stream()
                .filter(resource -> resource instanceof AbstractCommandAnnotationHandler)
                .map(resource -> (AbstractCommandAnnotationHandler<?>) resource)
                .collect(Collectors.toMap(AbstractCommandAnnotationHandler::getAnnotationType, processor -> processor)));
        //todo to new injector
    }

    public void modifyAll(CommandInfo commandInfo, AnnotatedElement annotatedElement) {
        for (Class<? extends Annotation> cls : getSortedAnnotations()) {
            modify(commandInfo, annotatedElement, cls);
        }
    }

    private <T extends Annotation> void modify(CommandInfo commandInfo, AnnotatedElement annotatedElement, Class<T> cls) {
        T annotation = annotatedElement.getDeclaredAnnotation(cls);
        AnnotationContext<T> context = new AnnotationContext<>(annotation, commandInfo);

        getHandler(cls).modify(context);
    }

    public List<AbstractCommandAnnotationHandler.Result> getFailedResults(CommandInfo commandInfo, CommandSession session) {
        List<AbstractCommandAnnotationHandler.Result> results = new ArrayList<>();

        for (Class<? extends Annotation> cls : getSortedAnnotations()) {
            Class<?> parentCls = commandInfo.getParent().getClass();
            Method handle = commandInfo.getHandle();

            Optional<AbstractCommandAnnotationHandler.Result> clsResult = getFailedResult(commandInfo, session, parentCls, cls);
            Optional<AbstractCommandAnnotationHandler.Result> methodResult = getFailedResult(commandInfo, session, handle, cls); //todo перенести

            clsResult.ifPresent(results::add);
            methodResult.ifPresent(results::add);
        }

        return results;
    }

    private <T extends Annotation> Optional<AbstractCommandAnnotationHandler.Result> getFailedResult(CommandInfo info, CommandSession session,
                                                                                                     AnnotatedElement annotatedElement, Class<T> cls) {
        T annotation = annotatedElement.getDeclaredAnnotation(cls);
        SessionAnnotationContext<T> context = new SessionAnnotationContext<>(annotation, info, session);

        AbstractCommandAnnotationHandler.Result result = getHandler(cls).verify(context);
        if (result.isError()) {
            return Optional.of(result);
        }

        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    private <T extends Annotation> AbstractCommandAnnotationHandler<T> getHandler(Class<T> cls) {
        return (AbstractCommandAnnotationHandler<T>) annotationProcessorsMap.get(cls);
    }

    private List<Class<? extends Annotation>> getSortedAnnotations() {
        return annotationProcessorsMap.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(entry -> getAnnotationHandler(entry.getClass()).priority()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private CommandAnnotationHandler getAnnotationHandler(Class<?> parentCls) {
        return CommandReflectionUtil.getAnnotation(parentCls, CommandAnnotationHandler.class);
    }
}
