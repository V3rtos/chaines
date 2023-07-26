package me.moonways.bridgenet.api.intercept;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Log4j2
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class ProxiedMethod {

    private static final String TO_STRING_FORMAT = "%s.%s();";

    public static ProxiedMethod create(Object source, Method method) {
        return new ProxiedMethod(source, method.getName(), method.getReturnType(), method);
    }

    private final Object source;

    private final String name;
    private final Class<?> returnType;

    private final Method declare;

    public synchronized Object call(@Nullable Object[] args) {
        if (source == null || source.getClass().isInterface()) {
            return null;
        }
        try {
            declare.setAccessible(true);
            return declare.invoke(source, args);
        }
        catch (IllegalAccessException | InvocationTargetException exception) {
            log.error("§4Cannot be invoke proxied method {}: §c{}", this, exception.toString());
            return null;
        }
    }

    public synchronized Object callEmpty() {
        return call(new Object[0]);
    }

    public synchronized boolean isVoid() {
        return returnType.equals(void.class);
    }

    public synchronized boolean hasAnnotation(Class<? extends Annotation> cls) {
        return declare.isAnnotationPresent(cls);
    }

    @Override
    public String toString() {
        return String.format(TO_STRING_FORMAT, source.getClass().getSimpleName(), name);
    }
}
