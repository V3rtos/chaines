package me.moonways.bridgenet.injection.scanner;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.Inject;
import me.moonways.bridgenet.injection.InjectionException;
import me.moonways.bridgenet.injection.PostFactoryMethod;
import me.moonways.bridgenet.injection.factory.ObjectFactory;
import me.moonways.bridgenet.injection.scanner.controller.ScannerController;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public final class DependencyScanner {

    private static final ExecutorService ASYNC_EXECUTOR = Executors.newCachedThreadPool();

    @Inject
    private DependencyInjection dependencyInjection;

    private final DependencyScannerContainer container = new DependencyScannerContainer();

    public void initContainer() {
        dependencyInjection.injectFields(container);
        container.initMaps();
    }

    private List<Class<?>> findOrdered(ScannerController scannerController, ScannerFilter scannerFilter) {
        if (scannerController == null) {

            log.error("§4Cannot be found dependencies for {}", scannerFilter);
            return Collections.emptyList();
        }

        Set<Class<?>> componentsSet = scannerController.findAllComponents(scannerFilter);
        return componentsSet.stream()
                .sorted(Comparator.comparingLong(cls ->
                        Arrays.stream(cls.getDeclaredFields())
                                .filter(field -> field.isAnnotationPresent(Inject.class))
                                .count()))
                .collect(Collectors.toList());
    }

    public void resolve(@NotNull String packageName, @NotNull Class<? extends Annotation> annotationType) {
        ScannerController scannerController = getScannerController(annotationType);
        ScannerFilter scannerFilter = ScannerFilter.create()
                .with(packageName)
                .with(annotationType);

        List<Class<?>> classesByAnnotationList = findOrdered(scannerController, scannerFilter);

        for (Class<?> componentClass : classesByAnnotationList) {
            scannerController.whenFound(dependencyInjection, componentClass);
        }
    }

    public void postFactoryMethods(Class<?> instanceClass, Object instance) {
        Method[] methods = instanceClass.getDeclaredMethods();

        for (Method method : methods) {
            PostFactoryMethod annotation = method.getDeclaredAnnotation(PostFactoryMethod.class);

            if (annotation == null) {
                continue;
            }

            method.setAccessible(true);

            boolean useAsync = annotation.async();

            invokeNativeInit(useAsync, method, instance);
            method.setAccessible(false);
        }
    }

    private void invokeNativeInit(boolean useAsync, Method method, Object instance) {
        Runnable invocationRunner = () -> {

            try {
                method.invoke(instance);
            }
            catch (Exception exception) {
                throw new InjectionException(exception);
            }
        };

        if (useAsync) {
            ASYNC_EXECUTOR.execute(invocationRunner);
        } else {
            invocationRunner.run();
        }
    }

    public ObjectFactory getObjectFactory(Class<? extends Annotation> cls) {
        return container.getObjectFactory(cls);
    }

    public ScannerController getScannerController(Class<? extends Annotation> cls) {
        return container.getScannerController(cls);
    }
}
