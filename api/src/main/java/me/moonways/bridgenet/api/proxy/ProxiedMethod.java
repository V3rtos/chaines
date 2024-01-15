package me.moonways.bridgenet.api.proxy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

@Log4j2
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class ProxiedMethod {

    private static final String TO_STRING_FORMAT = "%s.%s()";

    public static ProxiedMethod create(Object source, Method method) {
        return new ProxiedMethod(source, method.getName(), method.getReturnType(), method);
    }

    private final Object source;

    private final String name;
    private final Class<?> returnType;

    private final Method declare;

    @Setter
    private Object lastCallReturnedValue;

    public synchronized Object call(@Nullable Object[] args) {
        if (source == null || source.getClass().isInterface()) {
            return null;
        }
        try {
            declare.setAccessible(true);

            return lastCallReturnedValue = declare.invoke(source, args);
        }
        catch (IllegalAccessException | InvocationTargetException exception) {
            log.error("§4Cannot be invoke proxied method {}: §c{}", this, exception.toString());

            Throwable cause = exception.getCause();

            if (cause == null) {
                cause = exception;
            }

            throw new InterceptionException(cause);
        }
    }

    public synchronized Object callEmpty() {
        return call(new Object[0]);
    }

    public synchronized boolean isVoid() {
        return returnType.equals(Void.TYPE);
    }

    public synchronized boolean hasAnnotation(Class<? extends Annotation> cls) {
        return declare.isAnnotationPresent(cls);
    }

    public synchronized <A extends Annotation> A findAnnotation(Class<A> cls) {
        return declare.getDeclaredAnnotation(cls);
    }

    public synchronized Parameter[] getParameters() {
        return declare.getParameters();
    }

    public synchronized Parameter[] getParametersByAnnotation(Class<? extends Annotation> cls) {
        return Arrays.stream(declare.getParameters()).filter(param -> param.isAnnotationPresent(cls)).toArray(Parameter[]::new);
    }

    @Override
    public String toString() {
        return String.format(TO_STRING_FORMAT, source.getClass().getSimpleName(), name);
    }
}
