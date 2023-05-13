package me.moonways.bridgenet.service.inject;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Log4j2
public class DependencyInjection {

    public static final String BASE_PACKAGE_NAME = "me.moonways";

    private static final ExecutorService CACHED_THREADS_POOL = Executors.newCachedThreadPool();

    private final Map<Class<?>, Object> dependencyInstancesMap = new HashMap<>();
    private final Map<Class<?>, Class<? extends Annotation>> dependencyAnnotatedMap = new HashMap<>();

    private void validateDepend(Object depend) {
        if (depend == null) {
            throw new NullPointerException("depend");
        }
    }

    public Set<Object> getInjectedDependsByAnnotation(@NotNull Class<? extends Annotation> componentAnnotation) {
        return dependencyInstancesMap.keySet()
                .stream()
                .filter(cls -> dependencyAnnotatedMap.get(cls) == componentAnnotation)
                .map(dependencyInstancesMap::get)
                .collect(Collectors.toSet());
    }

    private Set<Class<?>> scanningPackages(@NotNull String packageName, @NotNull Class<? extends Annotation> componentAnnotation) {
        List<ClassLoader> classLoadersList = new LinkedList<>();

        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))

                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false), new ResourcesScanner())
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));

        return reflections.getTypesAnnotatedWith(componentAnnotation);
    }

    public void scanDependencies(@NotNull String packageName) {
        scanDependencies(packageName, Component.class);
    }

    public void scanDependenciesOfBasicPackage() {
        scanDependencies(BASE_PACKAGE_NAME);
    }

    public void scanDependenciesOfBasicPackage(@NotNull Class<? extends Annotation> componentAnnotation) {
        scanDependencies(BASE_PACKAGE_NAME, componentAnnotation);
    }

    @SneakyThrows
    public void scanDependencies(@NotNull String packageName, @NotNull Class<? extends Annotation> componentAnnotation) {
        List<Class<?>> classesByAnnotationList = scanningPackages(packageName, componentAnnotation)
                .stream()
                .sorted(Comparator.comparingLong(cls ->
                                Arrays.stream(cls.getDeclaredFields())
                                        .filter(field -> field.isAnnotationPresent(Inject.class))
                                        .count()))
                .collect(Collectors.toList());

        for (Class<?> dependencyClass : classesByAnnotationList) {
            if (dependencyClass == DependencyInjection.class) {

                addDepend(this);
                addDependAnnotated(dependencyClass, Component.class);
                continue;
            }

            if (dependencyInstancesMap.containsKey(dependencyClass))
                continue;

            Constructor<?> constructor = dependencyClass.getConstructor();
            constructor.setAccessible(true);

            Object dependencyInstance = constructor.newInstance();

            addDepend(dependencyClass, dependencyInstance);
            addDependAnnotated(dependencyClass, componentAnnotation);
        }

        HashMap<Class<?>, Object> clonedInstancesMap = new HashMap<>(dependencyInstancesMap);
        clonedInstancesMap.forEach((dependencyClass, dependencyInstance) -> {

            if (classesByAnnotationList.contains(dependencyClass)) {
                fireInitMethods(dependencyClass, dependencyInstance);
            }
        });
    }

    private void addDependAnnotated(@NotNull Class<?> dependClass, @NotNull Class<? extends Annotation> componentAnnotation) {
        dependencyAnnotatedMap.put(dependClass, componentAnnotation);
    }

    public void addDepend(@NotNull Object depend) {
        addDepend(depend.getClass(), depend);
    }

    public void addDepend(@NotNull Class<?> dependClass, @NotNull Object depend) {
        validateDepend(depend);

        injectDependencies(depend);
        dependencyInstancesMap.put(dependClass, depend);

        if (dependClass.isAnnotationPresent(Component.class))
            log.info("Founded dependency component - " + dependClass.getName());
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
