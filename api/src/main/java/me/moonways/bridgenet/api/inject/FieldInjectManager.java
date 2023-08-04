package me.moonways.bridgenet.api.inject;

import java.lang.annotation.Annotation;
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

    private final Queue<FieldQueueState> injectionQueue = new ArrayDeque<>();

    public void injectFields(@NotNull Object instance) {
        this.injectFields(instance, this.findInjectors(instance), container.getStoredClasses());
        this.injectProperties(instance, this.findPropertyFields(instance.getClass()));
    }

    private void injectFields(Object instance, Field[] fieldsArray, Set<Class<?>> dependenciesClasses) {
        FieldQueueState[] fieldsInQueueByDependency = findFieldsInQueueByDependency(instance.getClass());

        for (Field field : fieldsArray) {
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
                injectionQueue.offer(new FieldQueueState(field, instance));
            }
        }

        for (FieldQueueState fieldQueueState : fieldsInQueueByDependency) {
            injectDependency(fieldQueueState.instance, fieldQueueState.field, instance);
        }
    }

    private void injectProperties(Object instance, List<Field> propertyFieldsList) {
        for (Field field : propertyFieldsList) {

            Property annotation = field.getDeclaredAnnotation(Property.class);
            injectProperty(instance, field, annotation.value());
        }
    }

    private void injectDependency(Object instance, Field field, Object dependInstance) {
        try {
            field.setAccessible(true);
            field.set(instance, dependInstance);
        }
        catch (IllegalAccessException exception) {
            log.error(InjectionErrorMessages.CANNOT_INJECT_FIELD_TYPE, field, exception.toString());
        }
    }

    private void injectProperty(Object instance, Field field, String value) {
        Class<?> returnType = field.getType();
        if (!returnType.equals(Object.class) && !String.class.isAssignableFrom(returnType)) {
            throw new InjectionException("Property field " + instance.getClass().getSimpleName() + "." + field.getName() + " type is not accessible");
        }

        injectDependency(instance, field, System.getProperty(value));
    }

    private Field[] findInjectors(@NotNull Object instance) {
        Class<?> instanceClass = instance.getClass();
        List<Field> list = findInjectionFields(instanceClass);

        Class<?> superclass = instanceClass;
        while ((superclass = superclass.getSuperclass()) != null && superclass != Object.class) {
            list.addAll(findInjectionFields(superclass));
        }

        return list.toArray(new Field[0]);
    }

    private List<Field> getFieldsListByAnnotation(@NotNull Class<?> cls, @NotNull Class<? extends Annotation> annotationType) {
        List<Field> list = new ArrayList<>();
        Field[] instanceFields = cls.getDeclaredFields();

        for (Field field : instanceFields) {
            if (!field.isAnnotationPresent(annotationType)) {
                continue;
            }

            list.add(field);
        }

        return list;
    }

    private List<Field> findInjectionFields(@NotNull Class<?> cls) {
        return getFieldsListByAnnotation(cls, Inject.class);
    }

    private List<Field> findPropertyFields(@NotNull Class<?> cls) {
        return getFieldsListByAnnotation(cls, Property.class);
    }

    private FieldQueueState[] findFieldsInQueueByDependency(Class<?> dependencyClass) {
        return injectionQueue
                .stream()
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
