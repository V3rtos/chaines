package me.moonways.bridgenet.api.inject;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

@Autobind
public class DependencyCustomAnnotationHandler {

    @Inject
    private DependencyInjection injector;

    public Stream<Object> peekAnnotatedMembers(Class<? extends Annotation> annotation) {
        injector.searchByProject(annotation);
        return injector.getContainer().getStoredInstances(annotation).stream();
    }
}
