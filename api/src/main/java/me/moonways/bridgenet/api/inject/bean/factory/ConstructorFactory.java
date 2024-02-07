package me.moonways.bridgenet.api.inject.bean.factory;

import me.moonways.bridgenet.api.inject.bean.BeanException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorFactory implements BeanFactory {

    @Override
    public <T> T create(Class<T> cls) {
        try {
            Constructor<?> constructor = cls.getConstructor();
            constructor.setAccessible(true);

            //noinspection unchecked
            return (T) constructor.newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException exception) {
            throw new BeanException(exception);
        }
    }
}
