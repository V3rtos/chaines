package me.moonways.bridgenet.api.inject.factory;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.InjectionErrorMessages;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Log4j2
public class DependObjectFactory implements ObjectFactory {

    @Override
    public <T> T create(Class<T> cls) {
        try {
            Constructor<?> constructor = cls.getConstructor();
            constructor.setAccessible(true);

            //noinspection unchecked
            return (T) constructor.newInstance();
        }
        catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException exception) {
            log.error(InjectionErrorMessages.CANNOT_CREATE_OBJECT_INSTANCE, exception);
            return null;
        }
    }
}
