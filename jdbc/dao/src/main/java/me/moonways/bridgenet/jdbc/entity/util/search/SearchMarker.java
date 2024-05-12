package me.moonways.bridgenet.jdbc.entity.util.search;

import javassist.util.proxy.ProxyFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.jdbc.entity.DatabaseEntityException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
public final class SearchMarker<T> {

    public static final int UNLIMITED_LIMIT = -1;

    private final ProxiedParametersFounder<T> founder;

    @Getter
    private int limit = UNLIMITED_LIMIT;
    @Getter
    private Map<String, Object> expectationMap;

    public SearchMarker<T> withLimit(int limit) {
        this.limit = limit;
        return this;
    }

    private String findParameterId(Function<T, ?> parameterGetter) {
        founder.prepareProxy();
        parameterGetter.apply(founder.proxiedEntity);

        return founder.getLastInvocation()
                .orElseThrow(() -> new DatabaseEntityException("Marked parameter is not annotated as entity element"));
    }

    public <E> SearchMarker<T> withGet(Function<T, E> parameterGetter, E expected) {
        return with(findParameterId(parameterGetter), expected);
    }

    public SearchMarker<T> with(String name, Object expectedValue) {
        if (expectationMap == null) {
            expectationMap = Collections.synchronizedMap(new HashMap<>());
        }
        expectationMap.put(name.toLowerCase(), expectedValue);
        return this;
    }

    public boolean isExpectationAwait(String id) {
        if (expectationMap == null) {
            return false;
        }
        return expectationMap.containsKey(id.toLowerCase());
    }

    public Object getExpectation(String id) {
        if (expectationMap == null) {
            return null;
        }
        return expectationMap.get(id.toLowerCase());
    }

    @RequiredArgsConstructor
    public static class ProxiedParametersFounder<T> {

        private final Class<T> entityClass;

        private T proxiedEntity;
        private EntityMethodHandler proxy;

        private void prepareProxy() {
            if (proxy == null) {
                proxy = new EntityMethodHandler();
            }

            if (proxiedEntity == null) {
                ProxyFactory proxyFactory = new ProxyFactory();
                proxyFactory.setSuperclass(entityClass);

                try {
                    //noinspection unchecked
                    proxiedEntity = (T) proxyFactory.create(prepareConstructTypes(), prepareConstructValues(), proxy);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                         InvocationTargetException exception) {
                    throw new DatabaseEntityException("Can`t prepare a proxied entity", exception);
                }
            }
        }

        private Constructor<?> findOptimalConstructor() {
            return Arrays.stream(entityClass.getDeclaredConstructors())
                    .peek(constructor -> constructor.setAccessible(true))
                    .min(Comparator.comparingInt(Constructor::getParameterCount))
                    .orElse(null);
        }

        private Class<?>[] prepareConstructTypes() {
            return Optional.ofNullable(findOptimalConstructor()).map(Constructor::getParameterTypes)
                    .orElseGet(() -> new Class[0]);
        }

        private Object[] prepareConstructValues() {
            return Arrays.stream(prepareConstructTypes())
                    .map(EntityMethodHandler::defaultReturn)
                    .toArray(Object[]::new);
        }

        public Optional<String> getLastInvocation() {
            return Optional.ofNullable(proxy).flatMap(EntityMethodHandler::getInvocation);
        }
    }
}
