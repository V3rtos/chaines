package me.moonways.bridgenet.jdbc.entity.util.search;

import javassist.util.proxy.MethodHandler;
import me.moonways.bridgenet.api.util.reflection.ReflectionUtils;
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
        return ReflectionUtils.defaultValue(method.getReturnType());
    }
}
