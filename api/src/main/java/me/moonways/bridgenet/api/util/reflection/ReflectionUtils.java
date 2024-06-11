package me.moonways.bridgenet.api.util.reflection;

import lombok.experimental.UtilityClass;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactoryProviders;

import java.lang.reflect.*;
import java.security.AccessControlException;
import java.util.stream.Stream;

@UtilityClass
public class ReflectionUtils {

    public Object invoke(Object instance, String methodName, Class<?>[] params, Object[] args) {
        Class<?> instanceType = instance.getClass();
        try {
            Method method = instanceType.getDeclaredMethod(methodName, params);
            grantAccess(method);

            return method.invoke(instance, args);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException exception) {
            throw new BridgenetReflectionException(exception);
        }
    }

    public Object invoke(Object instance, String methodName, Object[] args) {
        return invoke(instance, methodName,
                Stream.of(args)
                        .map(Object::getClass)
                        .toArray(Class<?>[]::new), args);
    }

    public Object invoke(Object instance, String methodName) {
        return invoke(instance, methodName, new Object[0]);
    }

    public void setField(Object instance, String fieldName, Object value) {
        Class<?> instanceType = instance.getClass();
        try {
            Field field = instanceType.getDeclaredField(fieldName);

            grantAccess(field);
            field.set(instance, value);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new BridgenetReflectionException(exception);
        }
    }

    public Field getField(Object instance, String name) {
        try {
            return instance.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException exception) {
            throw new BridgenetReflectionException(exception);
        }
    }

    public Object newInstanceOf(Class cls) {
        Object instance = tryConstructInstanceOrNull(cls);
        if (instance == null) {
            return BeanFactoryProviders.UNSAFE.getImpl().get().create(cls);
        }
        return instance;
    }

    private Object tryConstructInstanceOrNull(Class<?> cls) {
        try {
            Constructor<?> constructor = cls.getConstructor();
            return constructor.newInstance();

        } catch (Exception exception) {
            return null;
        }
    }

    public Object defaultValue(Class<?> returnType) {
        if ((returnType.isPrimitive() && !returnType.equals(boolean.class)) || Number.class.isAssignableFrom(returnType)) {
            if (returnType.equals(long.class) || returnType.equals(Long.class)) { // fuck java cast
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

    public void grantAccess(AccessibleObject accessibleObject) {
        try {
            accessibleObject.setAccessible(true);
        } catch (Throwable ignored) {
            // ignore any exceptions.
        }
    }
}
