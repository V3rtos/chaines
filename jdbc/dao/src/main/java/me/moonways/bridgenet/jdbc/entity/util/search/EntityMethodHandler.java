package me.moonways.bridgenet.jdbc.entity.util.search;

import javassist.util.proxy.MethodHandler;
import me.moonways.bridgenet.jdbc.entity.util.EntityPersistenceUtil;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class EntityMethodHandler implements MethodHandler {

    private final AtomicReference<String> invokedIdRef = new AtomicReference<>();

    public Optional<String> getInvocation() {
        return Optional.ofNullable(invokedIdRef.get());
    }

    @Override
    public Object invoke(Object source, Method method, Method proceed, Object[] args) throws Throwable {
        invokedIdRef.set(null);
        if (EntityPersistenceUtil.isParameter(method)) {
            invokedIdRef.set(EntityPersistenceUtil.getParameterId(method));
        }
        return defaultReturn(method.getReturnType());
    }

    public static Object defaultReturn(Class<?> returnType) {
        if (returnType.isPrimitive() && !returnType.equals(boolean.class)) {
            if (returnType.equals(long.class)) { // fuck java cast
                return 0L;
            } else {
                return 0;
            }
        }
        if (Number.class.isAssignableFrom(returnType)) {
            if (returnType.equals(Long.class)) { // fuck java cast
                return 0L;
            } else {
                return 0;
            }
        }
        if (returnType.equals(boolean.class) || returnType.equals(Boolean.class)) {
            return false;
        }
        return null;
    }
}
