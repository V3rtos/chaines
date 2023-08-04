package me.moonways.bridgenet.api.inject;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class DependencyContainer {

    private final Map<Class<?>, Object> instancesMap = Collections.synchronizedMap(new HashMap<>());
    private final Set<Class<?>> ignoredInterfaces = Collections.synchronizedSet(new HashSet<>());

    private final Map<Class<?>, Class<? extends Annotation>> annotationsByComponentTypeMap = new HashMap<>();

    public Set<Object> getStoredInstances(@NotNull Class<? extends Annotation> annotation) {
        return instancesMap.keySet()
                .stream()
                .filter(cls -> annotationsByComponentTypeMap.get(cls) == annotation)
                .map(instancesMap::get)
                .collect(Collectors.toSet());
    }

    public Set<Class<?>> getStoredClasses() {
        return instancesMap.keySet();
    }

    public boolean isStored(Class<?> cls) {
        return instancesMap.containsKey(cls);
    }

    public void subscribeDependByAnnotation(@NotNull Class<?> dependClass, @NotNull Class<? extends Annotation> componentAnnotation) {
        annotationsByComponentTypeMap.put(dependClass, componentAnnotation);
    }

    public void store(@NotNull Class<?> cls, @NotNull Object object) {
        if (cls.isInterface() && isStored(cls)) {

            InjectionException exception = new InjectionException(InjectionErrorMessages.DUPLICATED_IMPLEMENTS);
            log.error(InjectionErrorMessages.DUPLICATED_IMPLEMENTS, cls.getName(), exception);
        }

        instancesMap.put(cls, object);

        Class<?>[] interfaces = cls.getInterfaces();
        if (interfaces.length == 1 && !interfaces[0].getPackage().getName().contains("java")) {

            Class<?> anInterface = interfaces[0];
            if (ignoredInterfaces.contains(anInterface)) {
                return;
            }

            if (isStored(anInterface)) {
                instancesMap.remove(anInterface);
                ignoredInterfaces.add(anInterface);
            } else {
                instancesMap.put(interfaces[0], object);
            }
        }
    }

    public void unbind(@NotNull Class<?> cls) {
        instancesMap.remove(cls);
    }

    public Object findInstance(Class<?> cls) {
        return instancesMap.get(cls);
    }
}
