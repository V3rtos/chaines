package me.moonways.bridgenet.api.inject.bean.factory;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.BeanException;
import me.moonways.bridgenet.api.inject.bean.service.BeansStore;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConstructorFactory implements BeanFactory {

    @Inject
    private BeansStore store;

    @SuppressWarnings("unchecked")
    private <T> Optional<Constructor<T>> lookupConstructor(Class<T> cls) {
        Constructor<T>[] constructorsArr = (Constructor<T>[]) cls.getConstructors();

        if (constructorsArr.length == 0) {
            return Optional.empty();
        }
        if (constructorsArr.length == 1) {
            return Optional.of(constructorsArr[0]);
        }

        List<Constructor<T>> list = Stream.of(constructorsArr).filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());

        if (list.size() > 1) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    @Override
    public <T> T create(Class<T> cls) {
        try {
            Constructor<T> constructor = lookupConstructor(cls)
                    .orElseThrow(() -> new BeanException("no constructor found for " + cls));
            constructor.setAccessible(true);

            Object[] args = parametersToArguments(constructor.getParameterTypes());
            return constructor.newInstance(args);

        } catch (InvocationTargetException | InstantiationException | IllegalAccessException exception) {
            throw new BeanException(exception);
        }
    }

    private Object[] parametersToArguments(Class<?>[] parameters) {
        return Stream.of(parameters).map(aClass -> store.find(aClass).orElse(null))
                .filter(Objects::nonNull)
                .map(Bean::getRoot).toArray(Object[]::new);
    }
}
