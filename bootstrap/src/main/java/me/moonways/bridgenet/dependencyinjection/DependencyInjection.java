package me.moonways.bridgenet.dependencyinjection;

import lombok.SneakyThrows;
import me.moonways.bridgenet.dependencyinjection.scanner.ResourceClasspathScanner;
import me.moonways.bridgenet.dependencyinjection.scanner.ResourceClasspathScannerResponse;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

@Depend
public class DependencyInjection {

    private final Map<Class<?>, Object> dependencyInstancesMap = new HashMap<>();

    private void validateDepend(Object depend) {
        if (depend == null) {
            throw new NullPointerException("depend");
        }
    }

    @SneakyThrows
    public void scanDependencies(@NotNull String packageName, @NotNull ResourceClasspathScanner classpathScanner) {
        ResourceClasspathScannerResponse scanningResponse = classpathScanner.find(packageName);
        Collection<Class<?>> classesByAnnotation = scanningResponse.getClassesByAnnotation(Depend.class);

        for (Class<?> dependencyClass : classesByAnnotation) {
            if (dependencyClass == DependencyInjection.class) {
                addDepend(this);
                continue;
            }

            Constructor<?> constructor = dependencyClass.getConstructor();
            constructor.setAccessible(true);

            addDepend(constructor.newInstance());
        }
    }

    public void addDepend(@NotNull Object depend) {
        validateDepend(depend);

        injectDependencies(depend);
        dependencyInstancesMap.put(depend.getClass(), depend);
    }

    public void injectDependencies(@NotNull Object instance) {
        Set<Class<?>> dependenciesClasses = dependencyInstancesMap.keySet();

        for (Class<?> dependencyClass : dependenciesClasses) {
            injectDependency(instance, dependencyClass, dependencyInstancesMap.get(dependencyClass));
        }
    }

    private void injectDependency(@NotNull Object instance, @NotNull Class<?> dependType, @NotNull Object dependInstance) {
        Field[] accessedToInjectFields = getAccessedToInjectFields(instance, dependType);
        for (Field field : accessedToInjectFields) {
            try {
                field.set(instance, dependInstance);
            }
            catch (IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    private Field[] getAccessedToInjectFields(@NotNull Object instance, @NotNull Class<?> dependType) {
        Set<Field> fieldsSet = new HashSet<>();

        Class<?> instanceClass = instance.getClass();
        Field[] instanceFields = instanceClass.getDeclaredFields();

        for (Field field : instanceFields) {
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }

            Class<?> type = field.getType();
            if (type.isAssignableFrom(dependType) && !type.isInterface()) {

                field.setAccessible(true);
                fieldsSet.add(field);
            }
        }

        return fieldsSet.toArray(new Field[0]);
    }
}
