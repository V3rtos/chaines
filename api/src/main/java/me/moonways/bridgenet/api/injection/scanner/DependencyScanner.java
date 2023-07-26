package me.moonways.bridgenet.api.injection.scanner;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.injection.scanner.controller.ScannerController;
import me.moonways.bridgenet.api.intercept.AnnotationInterceptor;
import me.moonways.bridgenet.api.injection.DependencyInjection;
import me.moonways.bridgenet.api.injection.Inject;
import me.moonways.bridgenet.api.injection.InjectionException;
import me.moonways.bridgenet.api.injection.PostFactoryMethod;
import me.moonways.bridgenet.api.injection.factory.ObjectFactory;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public final class DependencyScanner {

    @Inject
    private DependencyInjection dependencyInjection;

    @Inject
    private AnnotationInterceptor annotationInterceptor;

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

    public void resolve(@NotNull Class<? extends Annotation> annotationType,
                        @NotNull ScannerFilter filter) {

        final ScannerController scannerController = getScannerController(annotationType);
        List<Class<?>> classesByAnnotationList = findOrdered(scannerController, filter);

        for (Class<?> componentClass : classesByAnnotationList) {
            scannerController.whenFound(dependencyInjection, componentClass, annotationType);
        }
    }

    public void resolve(@NotNull String packageName, @NotNull Class<? extends Annotation> annotationType) {
        final ScannerFilter filter = ScannerFilter.create()
                .with(packageName)
                .with(annotationType);

        resolve(annotationType, filter);
    }

    public void postFactoryMethods(Class<?> instanceClass, Object instance) {
        postProxiedFactoryMethods(instanceClass, instance, null);
    }

    public void postProxiedFactoryMethods(Class<?> instanceClass, Object instance, Object proxy) {
        Method[] methods = instanceClass.getDeclaredMethods();

        for (Method method : methods) {
            PostFactoryMethod annotation = method.getDeclaredAnnotation(PostFactoryMethod.class);

            if (annotation == null) {
                continue;
            }

            method.setAccessible(true);
            invokeNativeInit(method, instance, proxy);
        }
    }

    private void invokeNativeInit(Method method, Object instance, Object proxy) {
        try {
            if (proxy != null) {
                annotationInterceptor.callProxiedSuperclassMethod(instance, proxy, method, new Object[0]);
            }
            else {
                method.invoke(instance);
            }
        }
        catch (Exception exception) {
            throw new InjectionException(exception);
        }
    }

    public ObjectFactory getObjectFactory(Class<? extends Annotation> cls) {
        return container.getObjectFactory(cls);
    }

    public ScannerController getScannerController(Class<? extends Annotation> cls) {
        return container.getScannerController(cls);
    }

    public String getSearchGeneralPackage() {
        return container.getGeneralPackage();
    }
}
