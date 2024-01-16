package me.moonways.bridgenet.api.modern_command.annotation;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_command.CommandInfo;
import me.moonways.bridgenet.api.modern_command.modern_annotation.persistance.UsageCooldown;

import java.lang.annotation.Annotation;
import java.util.List;

@Autobind
public class AnnotationService {

    @Inject
    private DependencyInjection dependencyInjection;

    @Inject
    private AnnotationRegistry registry;

    @Inject
    private AnnotationContainer container;

    public void register() {
        dependencyInjection.searchByProject(CustomAnnotation.class);

        List<RegistrationCommandAnnotationParameter> registrationCommandAnnotations = registry.init();

        for (RegistrationCommandAnnotationParameter registrationCommandAnnotation : registrationCommandAnnotations) {
            container.add(registrationCommandAnnotation.getAnnotationClass(), registrationCommandAnnotation.getRegistrationCommandAnnotation());
        }

        RegistrationCommandAnnotation<Annotation, ?> registrationCommandAnnotation = container.get(UsageCooldown.class);

        registrationCommandAnnotation.register(CommandInfo.class.getDeclaredAnnotation(UsageCooldown.class), null);
    }
}
