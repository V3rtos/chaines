package me.moonways.bridgenet.api.util.reflection;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

@UtilityClass
public class ReflectionUtils {

    public Object callMethod(Object instance, String methodName, Class<?>[] params, Object[] args) {
        Class<?> instanceType = instance.getClass();
        try {
            Method privateMethod = instanceType.getDeclaredMethod(methodName, params);
            privateMethod.setAccessible(true);

            return privateMethod.invoke(instance, args);
        }
        catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException exception) {
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
        }
        catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new BridgenetReflectionException(exception);
        }
    }
}
