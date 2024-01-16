package me.moonways.bridgenet.api.modern_command.annotation;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AnnotationContainer {

    private final Map<Class<? extends Annotation>, RegistrationCommandAnnotation<?, ?>> annotations = new HashMap<>();

    public void add(Class<? extends Annotation> annotationClass, RegistrationCommandAnnotation<?, ?> registrationCommandAnnotation) {
        annotations.put(annotationClass, registrationCommandAnnotation);
    }

    public RegistrationCommandAnnotation<?, ?> get(Class<? extends Annotation> annotationClass) {
        return annotations.get(annotationClass);
    }
}
