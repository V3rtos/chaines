package me.moonways.bridgenet.dependencyinjection;

import lombok.SneakyThrows;
import me.moonways.bridgenet.dependencyinjection.scanner.ResourceClasspathScanner;
import me.moonways.bridgenet.dependencyinjection.scanner.ResourceClasspathScannerResponse;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Depend
public class DependencyInjection {

    private static final ExecutorService CACHED_THREADS_POOL = Executors.newCachedThreadPool();

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

            Object dependencyInstance = constructor.newInstance();

            addDepend(dependencyClass, dependencyInstance);
        }

        dependencyInstancesMap.forEach((dependencyClass, dependencyInstance) -> {

            if (classesByAnnotation.contains(dependencyClass)) {
                fireInitMethods(dependencyClass, dependencyInstance);
            }
        });
    }

    public void addDepend(@NotNull Object depend) {
        addDepend(depend.getClass(), depend);
    }

    public void addDepend(@NotNull Class<?> dependClass, @NotNull Object depend) {
        validateDepend(depend);

        injectDependencies(depend);
        dependencyInstancesMap.put(dependClass, depend);
    }

    public void injectDependencies(@NotNull Object instance) {
        Set<Class<?>> dependenciesClasses = dependencyInstancesMap.keySet();

        for (Class<?> dependencyClass : dependenciesClasses) {
            injectDependency(instance, dependencyClass, dependencyInstancesMap.get(dependencyClass));
        }
    }

    private void injectDependency(Object instance, Class<?> dependType, Object dependInstance) {
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

    private Field[] getAccessedToInjectFields(Object instance, Class<?> dependType) {
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

    private void fireInitMethods(Class<?> instanceClass, Object instance) {
        Method[] methods = instanceClass.getDeclaredMethods();

        for (Method method : methods) {
            InitMethod annotation = method.getDeclaredAnnotation(InitMethod.class);
            if (annotation == null)
                continue;

            method.setAccessible(true);
            boolean asynchronousInitialization = annotation.asynchronousInitialization();

            fireInitMethod(asynchronousInitialization, method, instance);
            method.setAccessible(false);
        }
    }

    private void fireInitMethod(boolean asynchronous, Method method, Object instance) {
        Runnable fireRunnable = () -> {

            try {
                method.invoke(instance);
            }
            catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        };

        if (asynchronous)
            CACHED_THREADS_POOL.submit(fireRunnable);
        else
            fireRunnable.run();
    }
}
