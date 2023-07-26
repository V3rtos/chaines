package me.moonways.bridgenet.api.injection;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Log4j2
@RequiredArgsConstructor
public class FieldInjectManager implements Serializable {

    private static final long serialVersionUID = -8750667413607682309L;

    private final DependencyContainer container;

    public void injectFields(@NotNull Object instance) {
        Set<Class<?>> dependenciesClasses = container.getFoundComponentsTypes();

        for (Class<?> dependencyClass : dependenciesClasses) {
            injectDependency(instance, dependencyClass, container.findInstance(dependencyClass));
        }
    }

    private void injectDependency(@NotNull Object instance, @NotNull Class<?> dependType, @NotNull Object dependInstance) {
        Field[] accessedToInjectFields = findFieldsWithComponentType(instance, dependType);

        for (Field field : accessedToInjectFields) {
            try {
                field.setAccessible(true);
                field.set(instance, dependInstance);
            }
            catch (IllegalAccessException exception) {

                MessageFactory messageFactory = log.getMessageFactory();
                Message message = messageFactory.newMessage(InjectionErrorMessages.CANNOT_INJECT_FIELD_TYPE, field);

                log.error(message, exception);
            }
        }
    }

    private Field[] findFieldsWithComponentType(@NotNull Object instance, @NotNull Class<?> dependType) {
        Set<Field> fieldsSet = new HashSet<>();

        Class<?> instanceClass = instance.getClass();
        Field[] instanceFields = instanceClass.getDeclaredFields();

        for (Field field : instanceFields) {
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }

            Class<?> type = field.getType();

            if (type.isAssignableFrom(dependType)) {
                fieldsSet.add(field);
            }
        }

        return fieldsSet.toArray(new Field[0]);
    }
}
