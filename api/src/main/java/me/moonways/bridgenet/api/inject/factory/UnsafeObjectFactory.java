package me.moonways.bridgenet.api.inject.factory;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeObjectFactory implements ObjectFactory {

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

    @Override
    public <T> T create(Class<T> cls) {
        try {
            //noinspection unchecked
            return (T) UNSAFE.allocateInstance(cls);
        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
