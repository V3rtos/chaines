package me.moonways.bridgenet.api.modern_command.annotation;

import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AnnotationRegistry {

    @Inject
    private DependencyInjection dependencyInjection;

    public List<RegistrationCommandAnnotationParameter> init() {
        List<RegistrationCommandAnnotationParameter> registrationCommandAnnotations = new ArrayList<>();

        dependencyInjection.getContainer()
                .getStoredInstances(CustomAnnotation.class)
                .forEach(registrationAnnotationObject -> {
                    Type[] genericInterfaces = registrationAnnotationObject.getClass().getGenericInterfaces();

                    for (Type genericInterface : genericInterfaces) {
                        if (genericInterface instanceof ParameterizedType) {
                            ParameterizedType parameterizedType = (ParameterizedType) genericInterface;

                            // Проверяем, является ли текущий параметризованный тип интерфейсом RegistrationCommandAnnotation
                            if (parameterizedType.getRawType() == RegistrationCommandAnnotation.class) {
                                Type[] typeArguments = parameterizedType.getActualTypeArguments();

                                // Получаем тип A (Annotation)
                                Class<? extends Annotation> annotationClass = (Class<? extends Annotation>) typeArguments[0];

                                RegistrationCommandAnnotation<?, ?> registrationCommandAnnotation = (RegistrationCommandAnnotation<?, ?>) registrationAnnotationObject;

                                RegistrationCommandAnnotationParameter registrationCommandAnnotationParameter =
                                        new RegistrationCommandAnnotationParameter(registrationCommandAnnotation, annotationClass);

                                registrationCommandAnnotations.add(registrationCommandAnnotationParameter);
                            }
                        }
                    }
                });

        return registrationCommandAnnotations;
    }
}
