package me.moonways.bridgenet.api.inject;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

    public void injectFields(@NotNull Object instance) {
        Set<Class<?>> dependenciesClasses = container.getFoundComponentsTypes();
        Field[] fieldsWithInjectAnnotation = findFieldsWithInjectAnnotation(instance);

        for (Field field : fieldsWithInjectAnnotation) {
            Class<?> returnType = field.getType();

            if (dependenciesClasses.contains(returnType)) {
                injectDependency(instance, field, container.findInstance(returnType));
            }
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
}
