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
import java.util.function.Function;

@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public final class SearchMarker<T> {

    public static final int UNLIMITED_LIMIT = -1;

    private final ProxiedParametersFounder<T> founder;

    @Getter
    @ToString.Include
    private int limit = UNLIMITED_LIMIT;

    @Getter
    @ToString.Include
    private Map<String, SearchElement<?>> expectationMap;

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
        return withGet(parameterGetter, SearchElement.<E>builder()
                .expectation(expected)
                .build());
    }

    public <E> SearchMarker<T> withGet(Function<T, E> parameterGetter, SearchElement<E> element) {
        return with(findParameterId(parameterGetter), element);
    }

    public SearchMarker<T> with(String name, Object expected) {
        return with(name, SearchElement.builder()
                .expectation(expected)
                .build());
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
        if (element.getBinder() == null) {
            element = element.toBuilder()
                    .binder(ConditionBinder.AND)
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
