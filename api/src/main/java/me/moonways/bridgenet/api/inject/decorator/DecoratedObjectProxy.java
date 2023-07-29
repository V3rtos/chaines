package me.moonways.bridgenet.api.inject.decorator;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.proxy.InterceptionFactory;
import me.moonways.bridgenet.api.proxy.MethodHandler;
import me.moonways.bridgenet.api.proxy.MethodInterceptor;
import me.moonways.bridgenet.api.proxy.ProxiedMethod;

@Log4j2
@MethodInterceptor
public class DecoratedObjectProxy {

    private final DecoratedMethodScanner scanner = new DecoratedMethodScanner();

    @InterceptionFactory
    void onInit() { scanner.bindHandlers(); }

    private Object executeInvocation(DecoratorInvocation invocation) {
        Set<Class<?>> annotationsTypes = scanner.getAnnotationsTypes()
            .stream()
            .<Class<? extends Annotation>>map(cls -> cls.asSubclass(Annotation.class))
            .filter(invocation::hasAnnotation)
            .collect(Collectors.toSet());

        if (annotationsTypes.isEmpty()) {
            return invocation.callNative();
        }

        if (annotationsTypes.size() == 1) {
            Class<?> first = annotationsTypes.stream().findFirst().orElse(null);
            return handleAnnotation(first, invocation);
        }

        List<Class<?>> orderedInherits = findOrderedInherits(annotationsTypes);
        List<Class<?>> singletonList = annotationsTypes.stream()
            .filter(cls -> !orderedInherits.contains(cls))
            .collect(Collectors.toList());

        if (annotationsTypes.size() - orderedInherits.size() <= 0) {
            log.error("§4Decorator beginner was not found for {}", invocation);

            return invocation.callNative();
        }

        Class<?> beginnerAnnotationType = singletonList.stream().findFirst().orElse(null);
        return executeFinally(beginnerAnnotationType, invocation, orderedInherits);
    }

    private List<Class<?>> findOrderedInherits(Set<Class<?>> annotationTypes) {
        List<Class<?>> list = new ArrayList<>();

        for (Class<?> cls : annotationTypes) {

            Class<? extends Annotation> annotationType = cls.asSubclass(Annotation.class);
            Set<Class<?>> inheritsAnnotations = scanner.findInheritsAnnotations(annotationType);

            for (Class<?> inherit : inheritsAnnotations) {
                if (annotationTypes.contains(inherit)) {
                    list.add(0, inherit);
                }
            }
        }

        if (annotationTypes.size() - list.size() != 1) {
            list.addAll(annotationTypes
                .stream()
                .filter(cls -> !list.contains(cls))
                .collect(Collectors.toSet()));
        }

        return list.stream().limit(annotationTypes.size() - 1)
            .collect(Collectors.toList());
    }

    private Object handleAnnotation(Class<?> cls, DecoratorInvocation invocation) {
        Class<? extends Annotation> annotationType = cls.asSubclass(Annotation.class);

        DecoratedMethodHandler handler = scanner.findMethodHandler(annotationType);
        return handler.handleProxyInvocation(invocation);
    }

    private Object executeFinally(Class<?> beginnerAnnotationType, DecoratorInvocation invocation, List<Class<?>> orderedInherits) {
        Iterator<Class<?>> iterator = orderedInherits.iterator();
        while (iterator.hasNext()) {
            Class<?> currentInherit = iterator.next();
            Class<?> nextInherit = iterator.hasNext() ? iterator.next() : beginnerAnnotationType;

            invocation.setInvocationProcess(() -> {
                if (nextInherit == beginnerAnnotationType) {
                    invocation.setInvocationProcess(invocation::callNative);
                }

                return handleAnnotation(nextInherit, invocation);
            });

            handleAnnotation(currentInherit, invocation);
        }

        return invocation.getLastCallReturnedValue();
    }

    private boolean matchesDecoratorsConflicts(ProxiedMethod proxiedMethod) {
        for (Class<?> annotationType : scanner.getAnnotationsTypes()) {
            Class<? extends Annotation> annotation = annotationType.asSubclass(Annotation.class);

            if (proxiedMethod.hasAnnotation(annotation)) {
                Set<Class<?>> conflictedAnnotations = scanner.findConflictedAnnotations(annotation);

                for (Class<?> conflictedAnnotation : conflictedAnnotations) {
                    if (proxiedMethod.hasAnnotation(conflictedAnnotation.asSubclass(Annotation.class))) {
                        log.warn("§4Founded decorators conflict at {}: §c{} with {}", proxiedMethod,
                            "@" + annotation.getSimpleName(), "@" + conflictedAnnotation.getSimpleName());

                        return true;
                    }
                }
            }
        }

        return false;
    }

    @MethodHandler
    public Object handle(ProxiedMethod proxiedMethod, Object[] args) {
        if (matchesDecoratorsConflicts(proxiedMethod)) {
            return proxiedMethod.call(args);
        }

        DecoratorInvocation invocation = new DecoratorInvocation(proxiedMethod, args);
        return executeInvocation(invocation);
    }
}
