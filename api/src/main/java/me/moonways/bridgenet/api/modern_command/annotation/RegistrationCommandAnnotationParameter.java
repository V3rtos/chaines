package me.moonways.bridgenet.api.modern_command.annotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;

@RequiredArgsConstructor
@Getter
public class RegistrationCommandAnnotationParameter {

    private final RegistrationCommandAnnotation<?, ?> registrationCommandAnnotation;
    private final Class<? extends Annotation> annotationClass;
}
