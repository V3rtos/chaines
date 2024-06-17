package me.moonways.bridgenet.api.inject.bean.factory.type;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.BeanException;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactory;
import me.moonways.bridgenet.api.inject.bean.service.BeansStore;
import me.moonways.bridgenet.api.util.reflection.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Log4j2
public class FactoryMethodFactory implements BeanFactory {

    @Inject
    private BeansStore store;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(Class<T> cls) {
        try {
            Method factoryMethod = lookupFactoryMethod(cls);
            if (factoryMethod == null) {
                return null;
            }

            ReflectionUtils.grantAccess(factoryMethod);
            Object[] args = parametersToArguments(factoryMethod.getParameterTypes());

            return (T) factoryMethod.invoke(null, args);
        } catch (Exception exception) {
            throw new BeanException(cls.getName(), exception);
        }
    }

    private Method lookupFactoryMethod(Class<?> cls) {
        List<Method> factoryMethodsResult = new ArrayList<>();

        for (Method method : cls.getDeclaredMethods()) {
            if (method.isAnnotationPresent(me.moonways.bridgenet.api.inject.BeanFactory.class)) {

                if (Modifier.isStatic(method.getModifiers()) && cls.isAssignableFrom(method.getReturnType())) {
                    factoryMethodsResult.add(method);
                }
            }
        }

        if (factoryMethodsResult.isEmpty()) {
            log.error("§4No one found factory method for {}", cls);
            return null;
        }

        if (factoryMethodsResult.size() > 1) {
            log.error("§4Founded {} factory methods for {} §7[must be 1 only]", factoryMethodsResult.size(), cls);
            return null;
        }

        return factoryMethodsResult.iterator().next();
    }

    private Object[] parametersToArguments(Class<?>[] parameters) {
        if (parameters.length == 0) {
            return new Object[0];
        }
        return Stream.of(parameters).map(aClass -> store.find(aClass).orElse(null))
                .map(bean -> bean == null ? null : bean.getRoot())
                .toArray(Object[]::new);
    }
}
