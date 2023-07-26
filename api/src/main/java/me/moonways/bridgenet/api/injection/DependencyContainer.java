package me.moonways.bridgenet.api.injection;

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
    private final Set<Class<?>> injectedClasses = Collections.synchronizedSet(new HashSet<>());

    private final Map<Class<?>, Class<? extends Annotation>> annotationsByComponentTypeMap = new HashMap<>();

    public Set<Object> getFoundComponents(@NotNull Class<? extends Annotation> componentAnnotation) {
        return instancesMap.keySet()
                .stream()
                .filter(cls -> annotationsByComponentTypeMap.get(cls) == componentAnnotation)
                .map(instancesMap::get)
                .collect(Collectors.toSet());
    }

    public void markInjected(Class<?> cls) {
        injectedClasses.add(cls);
    }

    public boolean hasInjectionMark(Class<?> cls) {
        return injectedClasses.contains(cls);
    }

    public Set<Class<?>> getFoundComponentsTypes() {
        return instancesMap.keySet();
    }

    public boolean isComponentFound(Class<?> cls) {
        return instancesMap.containsKey(cls);
    }

    public void addComponentWithAnnotation(@NotNull Class<?> dependClass, @NotNull Class<? extends Annotation> componentAnnotation) {
        annotationsByComponentTypeMap.put(dependClass, componentAnnotation);
    }

    public void store(@NotNull Class<?> cls, @NotNull Object object) {
        if (cls.isInterface() && isComponentFound(cls)) {

            InjectionException exception = new InjectionException(InjectionErrorMessages.DUPLICATED_IMPLEMENTS);
            log.error(InjectionErrorMessages.DUPLICATED_IMPLEMENTS, exception);
        }

        instancesMap.put(cls, object);
    }

    public void remove(@NotNull Class<?> cls) {
        instancesMap.remove(cls);
    }

    public void executeComponentsEach(BiConsumer<Class<?>, Object> foreachConsumer) {
        instancesMap.forEach(foreachConsumer);
    }

    public Object findInstance(Class<?> cls) {
        return instancesMap.get(cls);
    }
}
