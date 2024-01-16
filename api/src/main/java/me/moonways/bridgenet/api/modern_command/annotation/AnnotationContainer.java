package me.moonways.bridgenet.api.modern_command.annotation;

import me.moonways.bridgenet.api.modern_command.StandardCommandInfo;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AnnotationContainer {

    private final Map<Class<? extends Annotation>, RegistrationCommandAnnotation<?, ?>> annotations = new HashMap<>();

    public void add(Class<? extends Annotation> annotationClass, RegistrationCommandAnnotation<?, ?> registrationCommandAnnotation) {
        annotations.put(annotationClass, registrationCommandAnnotation);
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation, C extends StandardCommandInfo> RegistrationCommandAnnotation<A, C> get(Class<? extends Annotation> annotationClass) {
        return (RegistrationCommandAnnotation<A, C>) annotations.get(annotationClass);
    }
}
