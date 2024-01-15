package me.moonways.bridgenet.api.inject.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.*;
import me.moonways.bridgenet.api.inject.factory.ObjectFactory;
import me.moonways.bridgenet.api.inject.scanner.controller.ScannerController;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import org.jetbrains.annotations.NotNull;

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

        LinkedList<Class<?>> totalComponentsList = new LinkedList<>(scannerController.findAllComponents(scannerFilter));
        totalComponentsList.sort(Comparator.comparing(component -> component.getDeclaredFields().length));

        setInjectionQueue(totalComponentsList);

        return totalComponentsList;
    }

    private void setInjectionQueue(LinkedList<Class<?>> totalComponentsList) {
        int componentIndex = 0;
        for (Class<?> component : new ArrayList<>(totalComponentsList) /*fix CME*/) {
            for (Field field : component.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Inject.class)) {
                    continue;
                }

                Class<?> injectionType = field.getType();
                int injectionIndex = totalComponentsList.indexOf(injectionType);

                if (injectionIndex > componentIndex) {
                    totalComponentsList.remove(component);
                    totalComponentsList.add(component);
                    break;
                }
            }

            componentIndex++;
        }
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

    public void processPreConstructs(Class<?> instanceClass) {
        Method[] methods = instanceClass.getDeclaredMethods();

        for (Method method : methods) {
            PreConstruct annotation = method.getDeclaredAnnotation(PreConstruct.class);

            if (annotation == null) {
                continue;
            }

            if ((method.getModifiers() & Modifier.STATIC) != Modifier.STATIC) {
                throw new InjectionException("@PreConstruct method " + instanceClass.getSimpleName() + "#" + method.getName() + " cannot be use because that is not static");
            }

            invokeNativeInitStatic(method);
        }
    }

    public void processPostConstruct(Class<?> instanceClass, Object instance) {
        Method[] methods = instanceClass.getDeclaredMethods();

        for (Method method : methods) {
            PostConstruct annotation = method.getDeclaredAnnotation(PostConstruct.class);

            if (annotation == null) {
                continue;
            }

            invokeNativeInit(method, instance);
        }
    }

    private void invokeNativeInit(Method method, Object instance) {
        try {
            method.setAccessible(true);
            method.invoke(instance);
        }
        catch (Exception exception) {
            System.out.println(method);
            System.out.println(instance);
            throw new InjectionException(exception);
        }
    }

    private void invokeNativeInitStatic(Method method) {
        try {
            method.setAccessible(true);
            method.invoke(null);
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
