package me.moonways.bridgenet.jdbc.entity.util;

import lombok.experimental.UtilityClass;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

@UtilityClass
public class JavaReflectionUtil {

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

    public void setFieldValue(Object instance, String name, Object value) {
        Field field = getField(instance, name);
        field.setAccessible(true);

        try {
            field.set(instance, value);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Field getField(Object instance, String name) {
        try {
            return instance.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Object createInstance(Class<?> cls) {
        Object instance = tryCreateInstanceByConstructor(cls);
        if (instance == null) {
            try {
                return UNSAFE.allocateInstance(cls);
            } catch (InstantiationException exception) {
                throw new RuntimeException(exception);
            }
        }
        return instance;
    }

    private Object tryCreateInstanceByConstructor(Class<?> cls) {
        try {
            Constructor<?> constructor = cls.getConstructor();
            return constructor.newInstance();

        } catch (Exception exception) {
            return null;
        }
    }
}
