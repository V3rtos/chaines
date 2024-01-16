package me.moonways.bridgenet.api.inject.factory;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.InjectionErrorMessages;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Log4j2
public class DependObjectFactory implements ObjectFactory {

    @Inject
    private DependencyInjection injector;

    @Override
    public <T> T create(Class<T> cls) {
        try {
            Constructor<?> constructor = cls.getConstructor();
            constructor.setAccessible(true);

            //noinspection unchecked
            T instance = (T) constructor.newInstance();

            injector.injectFields(instance);
            return instance;
        }
        catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException exception) {
            log.error(InjectionErrorMessages.CANNOT_CREATE_OBJECT_INSTANCE, cls.getName(), exception);
            return null;
        }
    }
}
