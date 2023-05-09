package me.moonways.bridgenet.api.inject;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class DependencyInjection {

    private static final ExecutorService CACHED_THREADS_POOL = Executors.newCachedThreadPool();

    private final Map<Class<?>, Object> dependencyInstancesMap = new HashMap<>();

    private void validateDepend(Object depend) {
        if (depend == null) {
            throw new NullPointerException("depend");
        }
    }

    private Set<Class<?>> scanningPackages(@NotNull String packageName) {
        List<ClassLoader> classLoadersList = new LinkedList<>();

        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))

                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false), new ResourcesScanner())
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));

        return reflections.getTypesAnnotatedWith(Depend.class);
    }

    @SneakyThrows
    public void scanDependencies(@NotNull String packageName) {
        List<Class<?>> classesByAnnotationList = scanningPackages(packageName)
                .stream()
                .sorted(Comparator.comparingLong(cls ->
                                Arrays.stream(cls.getDeclaredFields())
                                        .filter(field -> field.isAnnotationPresent(Inject.class))
                                        .count()))
                .collect(Collectors.toList());

        for (Class<?> dependencyClass : classesByAnnotationList) {
            if (dependencyClass == DependencyInjection.class) {
                addDepend(this);
                continue;
            }

            if (dependencyInstancesMap.containsKey(dependencyClass))
                continue;

            Constructor<?> constructor = dependencyClass.getConstructor();
            constructor.setAccessible(true);

            Object dependencyInstance = constructor.newInstance();

            addDepend(dependencyClass, dependencyInstance);
        }

        dependencyInstancesMap.forEach((dependencyClass, dependencyInstance) -> {

            if (classesByAnnotationList.contains(dependencyClass)) {
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
