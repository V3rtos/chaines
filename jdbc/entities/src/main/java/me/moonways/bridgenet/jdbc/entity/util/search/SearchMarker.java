package me.moonways.bridgenet.jdbc.entity.util.search;

import javassist.util.proxy.ProxyFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.util.reflection.ReflectionUtils;
import me.moonways.bridgenet.jdbc.core.compose.ConditionBinder;
import me.moonways.bridgenet.jdbc.core.compose.ConditionMatcher;
import me.moonways.bridgenet.jdbc.entity.DatabaseEntityException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public final class SearchMarker<T> {

    public static final int UNLIMITED_LIMIT = -1;
    private static final Map<Class<?>, ProxiedParametersFounder<?>> foundersMap = new ConcurrentHashMap<>();

    private final Class<T> entityClass;

    @Getter
    @ToString.Include
    private int limit = UNLIMITED_LIMIT;

    @Getter
    @ToString.Include
    private Map<String, SearchElement<?>> expectationMap;

    public SearchMarker<T> limit(int limit) {
        this.limit = limit;
        return this;
    }

    @SuppressWarnings("unchecked")
    private String findParameterId(Function<T, ?> parameterGetter) {
        ProxiedParametersFounder<T> founder = (ProxiedParametersFounder<T>) foundersMap.computeIfAbsent(entityClass,
                k -> new ProxiedParametersFounder<>(entityClass));

        founder.prepareProxy();
        parameterGetter.apply(founder.proxiedEntity);

        return founder.getLastInvocation()
                .orElseThrow(() -> new DatabaseEntityException("Marked parameter is not annotated as entity-column"));
    }

    public <E> SearchMarker<T> and(Function<T, E> parameterGetter, E expected) {
        return with(findParameterId(parameterGetter), SearchElement.<E>builder()
                .binder(ConditionBinder.AND)
                .expectation(expected)
                .build());
    }

    public SearchMarker<T> and(String name, Object expected) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.AND)
                .expectation(expected)
                .build());
    }

    public <E> SearchMarker<T> or(Function<T, E> parameterGetter, E expected) {
        return with(findParameterId(parameterGetter), SearchElement.<E>builder()
                .binder(ConditionBinder.OR)
                .expectation(expected)
                .build());
    }

    public SearchMarker<T> or(String name, Object expected) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.OR)
                .expectation(expected)
                .build());
    }

    public <E> SearchMarker<T> with(Function<T, E> parameterGetter, SearchElement<E> element) {
        return with(findParameterId(parameterGetter), element);
    }

    public <E> SearchMarker<T> with(String name, SearchElement<E> element) {
        if (element.getExpectation() == null) {
            throw new NullPointerException("SearchElement#getExpectation() not filled");
        }

        if (expectationMap == null) {
            expectationMap = Collections.synchronizedMap(new HashMap<>());
        }

        if (element.getMatcher() == null) {
            element = element.toBuilder()
                    .matcher(ConditionMatcher.EQUALS)
                    .build();
        }
        expectationMap.put(name.toLowerCase(), element);
        return this;
    }

    public boolean isExpectationAwait(String id) {
        if (expectationMap == null) {
            return false;
        }
        return expectationMap.containsKey(id.toLowerCase());
    }

    public SearchElement<?> getExpectation(String id) {
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
                    .peek(ReflectionUtils::grantAccess)
                    .min(Comparator.comparingInt(Constructor::getParameterCount))
                    .orElse(null);
        }

        private Class<?>[] prepareConstructTypes() {
            return Optional.ofNullable(findOptimalConstructor()).map(Constructor::getParameterTypes)
                    .orElseGet(() -> new Class[0]);
        }

        private Object[] prepareConstructValues() {
            return Arrays.stream(prepareConstructTypes())
                    .map(ReflectionUtils::defaultValue)
                    .toArray(Object[]::new);
        }

        public Optional<String> getLastInvocation() {
            return Optional.ofNullable(proxy).flatMap(EntityMethodHandler::getInvocation);
        }
    }
}
