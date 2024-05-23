package me.moonways.bridgenet.api.util.reflection;

import lombok.experimental.UtilityClass;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

@UtilityClass
public class ReflectionUtils {

    private static final Unsafe UNSAFE;

    static {
        try {
            Field unsafeInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeInstanceField.setAccessible(true);

            UNSAFE = ((Unsafe) unsafeInstanceField.get(null));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public Object callMethod(Object instance, String methodName, Class<?>[] params, Object[] args) {
        Class<?> instanceType = instance.getClass();
        try {
            Method privateMethod = instanceType.getDeclaredMethod(methodName, params);
            privateMethod.setAccessible(true);

            return privateMethod.invoke(instance, args);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException exception) {
            throw new BridgenetReflectionException(exception);
        }
    }

    public Object callMethod(Object instance, String methodName, Object[] args) {
        return callMethod(instance, methodName,
                Stream.of(args)
                        .map(Object::getClass)
                        .toArray(Class<?>[]::new), args);
    }

    public Object callMethod(Object instance, String methodName) {
        return callMethod(instance, methodName, new Object[0]);
    }

    public void setField(Object instance, String fieldName, Object value) {
        Class<?> instanceType = instance.getClass();
        try {
            Field field = instanceType.getDeclaredField(fieldName);

            field.setAccessible(true);
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

    public Object createInstance(Class cls) {
        Object instance = tryConstructInstanceOrNull(cls);
        if (instance == null) {
            try {
                return UNSAFE.allocateInstance(cls);
            } catch (InstantiationException exception) {
                throw new BridgenetReflectionException(exception);
            }
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
