package me.moonways.bridgenet.api.inject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;

@Log4j2
@RequiredArgsConstructor
public class FieldInjectManager implements Serializable {

    private static final long serialVersionUID = -8750667413607682309L;

    private final DependencyContainer container;

    private final Queue<FieldQueueState> queue = new ArrayDeque<>();

    public void injectFields(@NotNull Object instance) {
        Set<Class<?>> dependenciesClasses = container.getFoundComponentsTypes();
        Field[] fieldsWithInjectAnnotation = findFieldsWithInjectAnnotation(instance);

        FieldQueueState[] fieldsInQueueByDependency = findFieldsInQueueByDependency(instance.getClass());

        for (Field field : fieldsWithInjectAnnotation) {
            Class<?> returnType = field.getType();

            if (dependenciesClasses.contains(returnType)) {
                injectDependency(instance, field, container.findInstance(returnType));
            }
            else {
                // try self-injection
                if (returnType.isAssignableFrom(instance.getClass())) {
                    injectDependency(instance, field, instance);
                    return;
                }

                // or else offer to injection-queue
                queue.offer(new FieldQueueState(field, instance));
            }
        }

        for (FieldQueueState fieldQueueState : fieldsInQueueByDependency) {
            injectDependency(fieldQueueState.instance, fieldQueueState.field, instance);
        }
    }

    private void injectDependency(@NotNull Object instance, @NotNull Field field, @NotNull Object dependInstance) {
        try {
            field.setAccessible(true);
            field.set(instance, dependInstance);
        }
        catch (IllegalAccessException exception) {
            log.error(InjectionErrorMessages.CANNOT_INJECT_FIELD_TYPE, field, exception.toString());
        }
    }

    private Field[] findFieldsWithInjectAnnotation(@NotNull Object instance) {
        Class<?> instanceClass = instance.getClass();
        List<Field> list = findInjectionFields(instanceClass);

        Class<?> superclass = instanceClass;
        while ((superclass = superclass.getSuperclass()) != null && superclass != Object.class) {
            list.addAll(findInjectionFields(superclass));
        }

        return list.toArray(new Field[0]);
    }

    private List<Field> findInjectionFields(@NotNull Class<?> cls) {
        List<Field> list = new ArrayList<>();
        Field[] instanceFields = cls.getDeclaredFields();

        for (Field field : instanceFields) {
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }

            list.add(field);
        }

        return list;
    }

    private FieldQueueState[] findFieldsInQueueByDependency(Class<?> dependencyClass) {
        return queue.stream()
            .filter(state -> state.field.getType().isAssignableFrom(dependencyClass))
            .toArray(FieldQueueState[]::new);
    }

    @ToString
    @RequiredArgsConstructor
    private static class FieldQueueState {

        private final Field field;
        private final Object instance;
    }
}
