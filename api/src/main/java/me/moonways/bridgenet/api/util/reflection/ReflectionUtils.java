package me.moonways.bridgenet.api.util.reflection;

import lombok.experimental.UtilityClass;
import me.moonways.bridgenet.api.inject.bean.factory.FactoryType;
import me.moonways.bridgenet.api.util.pair.Pair;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.util.*;

@UtilityClass
public class ReflectionUtils {

    private static final Unsafe unsafe;

    private static final long OVERRIDE_OFFSET;

    static {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);

            unsafe = (Unsafe) unsafeField.get(null);

            OVERRIDE_OFFSET = unsafe.objectFieldOffset(AccessibleObject.class.getDeclaredField("override"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    private static final Map<Pair<Class<?>, Integer>, Constructor<?>> reflectionConstructorsCache = Collections.synchronizedMap(new HashMap<>());
    private static final Map<Pair<Class<?>, String>, Method> reflectionMethodsCache = Collections.synchronizedMap(new HashMap<>());
    private static final Map<Pair<Class<?>, String>, Field> reflectionFieldsCache = Collections.synchronizedMap(new HashMap<>());

    private static final Map<Method, MethodHandle> methodToMethodHandleMap = Collections.synchronizedMap(new HashMap<>());

    private Constructor<?> getConstructor(Class<?> cls, int index, Class<?>[] argsTypes) {
        Pair<Class<?>, Integer> key = Pair.immutable(cls, index);
        return reflectionConstructorsCache.computeIfAbsent(key, k -> {
            try {
                return cls.getDeclaredConstructor(argsTypes);
            } catch (NoSuchMethodException exception) {
                throw new BridgenetReflectionException(exception);
            }
        });
    }

    private Method getMethod(Class<?> cls, String name, Class<?>[] argsTypes) {
        Pair<Class<?>, String> key = Pair.immutable(cls, name);
        return reflectionMethodsCache.computeIfAbsent(key, k -> {
            try {
                return cls.getDeclaredMethod(name, argsTypes);
            } catch (NoSuchMethodException exception) {
                throw new BridgenetReflectionException(exception);
            }
        });
    }

    private Field getField(Class<?> cls, String name) {
        Pair<Class<?>, String> key = Pair.immutable(cls, name);
        return reflectionFieldsCache.computeIfAbsent(key, k -> {
            try {
                return cls.getDeclaredField(name);
            } catch (NoSuchFieldException exception) {
                throw new BridgenetReflectionException(exception);
            }
        });
    }

    private MethodHandle getMethodHandle(Method method) {
        return methodToMethodHandleMap.computeIfAbsent(method, k -> {
            try {
                return lookup.unreflect(method);
            } catch (IllegalAccessException exception) {
                throw new BridgenetReflectionException(exception);
            }
        });
    }

    public Object invoke(Object instance, String methodName, Class<?>[] params, Object[] args) {
        Method method = getMethod(instance.getClass(), methodName, params);
        return invoke(instance, method, args);
    }

    public Object invoke(Object instance, String methodName, Object[] args) {
        Class<?>[] argTypes = new Class[args.length];
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
        }
        return invoke(instance, methodName, argTypes, args);
    }

    public Object invoke(Object instance, String methodName) {
        return invoke(instance, methodName, new Object[0]);
    }

    public Object invoke(Object instance, Method method) {
        return invoke(instance, method, new Object[0]);
    }

    public Object invoke(Object instance, Method method, Object[] args) {
        List<Object> argumentsList = new ArrayList<>();
        if (!Modifier.isStatic(method.getModifiers())) {
            argumentsList.add(instance);
        }
        argumentsList.addAll(Arrays.asList(args));

        grantAccess(method);
        try {
            MethodHandle methodHandle = getMethodHandle(method);
            return methodHandle.invokeWithArguments(argumentsList);
        } catch (Throwable e) {
            throw new BridgenetReflectionException(e);
        }
    }

    public void setField(Object instance, String fieldName, Object value) {
        Field field = getField(instance.getClass(), fieldName);
        unsafe.putObject(instance, unsafe.objectFieldOffset(field), value);
    }

    public void setField(Object instance, Field field, Object value) {
        unsafe.putObject(instance, unsafe.objectFieldOffset(field), value);
    }

    public Field getField(Object instance, String name) {
        return getField(instance.getClass(), name);
    }

    public Object newInstanceOf(Class cls) {
        Object instance = tryConstructInstanceOrNull(cls);
        if (instance == null) {
            return FactoryType.UNSAFE.get().create(cls);
        }
        return instance;
    }

    private Object tryConstructInstanceOrNull(Class<?> cls) {
        try {
            Constructor<?> constructor = getConstructor(cls, -1, new Class[0]);
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
            unsafe.putBoolean(accessibleObject, OVERRIDE_OFFSET, true);
        } catch (Throwable ignored) {
            // ignore any exceptions.
        }
    }
}
