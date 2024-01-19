package me.moonways.bridgenet.api.inject;

import java.lang.annotation.Annotation;
import java.util.*;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.function.Consumer;

@Log4j2
@RequiredArgsConstructor
public class FieldInjectManager implements Serializable {

    private static final long serialVersionUID = -8750667413607682309L;

    private final DependencyContainer container;

    private final Queue<QueuedInjection> injectionQueue = new ArrayDeque<>();
    private final Map<Class<?>, Consumer<Object>> postInjectionQueueLeaveTasks = new HashMap<>();

    private QueuedInjection[] previousInjectionQueueArray;

    public void injectFields(@NotNull Object instance) {
        this.injectFields(instance, this.findInjectors(instance), container.getStoredClasses());
        this.injectProperties(instance, this.findPropertyFields(instance.getClass()));
    }

    private void injectFields(Object instance, Field[] fieldsArray, Set<Class<?>> dependenciesClasses) {
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
                injectionQueue.offer(new QueuedInjection(field, instance));
            }
        }
    }

    public void flushInjectionsQueue(Object instance) {
        QueuedInjection[] fieldsInQueueByDependency = findFieldsInQueueByDependency(instance.getClass());

        for (QueuedInjection injection : fieldsInQueueByDependency) {
            injectFields(injection.instance);
            injectionQueue.remove(injection);

            Class<?> instanceClass = injection.instance.getClass();
            if (!isInInjectionQueue(instanceClass)) {

                Consumer<Object> postTask = postInjectionQueueLeaveTasks.get(instanceClass);

                if (postTask != null) {
                    postTask.accept(injection.instance);
                }
            }
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

    private QueuedInjection[] findFieldsInQueueByDependency(Class<?> dependencyClass) {
        QueuedInjection[] previousInjectionQueueArray = this.previousInjectionQueueArray;
        QueuedInjection[] newInjectionQueue = injectionQueue.stream()
                .filter(state -> state.field.getType().isAssignableFrom(dependencyClass))
                .toArray(QueuedInjection[]::new);

        if (previousInjectionQueueArray == null) {
            previousInjectionQueueArray = newInjectionQueue;
        }

        return previousInjectionQueueArray;
    }

    public boolean isInInjectionQueue(Class<?> cls) {
        return injectionQueue.stream()
                .anyMatch(state -> state.instance.getClass().isAssignableFrom(cls));
    }

    public void addPostInjectionQueueLeaveTask(Class<?> cls, Consumer<Object> task) {
        Consumer<Object> currentTask = postInjectionQueueLeaveTasks.get(cls);
        if (currentTask == null) {
            currentTask = task;
        } else {
            currentTask = currentTask.andThen(task);
        }
        postInjectionQueueLeaveTasks.put(cls, currentTask);
    }

    @ToString
    @EqualsAndHashCode
    @RequiredArgsConstructor
    private static class QueuedInjection {

        private final Field field;
        private final Object instance;
    }
}
