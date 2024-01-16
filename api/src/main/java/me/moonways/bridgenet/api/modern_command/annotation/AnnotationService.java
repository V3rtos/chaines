package me.moonways.bridgenet.api.modern_command.annotation;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_command.CommandInfo;
import me.moonways.bridgenet.api.modern_command.Interval;

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

        RegistrationCommandAnnotation<?, ?> registrationCommandAnnotation = container.get(Interval.class);

        registrationCommandAnnotation.register(CommandInfo.class.getDeclaredAnnotation(Interval.class), null);
    }
}
