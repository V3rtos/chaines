package me.moonways.bridgenet.jdbc.entity.criteria;

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

/**
 * Класс для создания критериев поиска сущностей в базе данных.
 * <p>
 * Позволяет задавать различные условия поиска с использованием методов
 * сравнения, логических операторов и ограничений на количество результатов.
 * </p>
 * <p>
 * Примеры использования:
 * <pre>{@code
 * SearchCriteria<MyEntity> criteria = new SearchCriteria<>(MyEntity.class);
 *
 * criteria.andEquals(MyEntity::getName, "John")
 *         .orMore(MyEntity::getAge, 30)
 *         .limit(10);
 *
 * List<MyEntity> results = repository.search(criteria).blockAll();
 * results.forEach(System.out::println);
 * }</pre>
 * </p>
 *
 * @param <T> Тип сущности.
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public final class SearchCriteria<T> {

    public static final int UNLIMITED_LIMIT = -1;
    private static final Map<Class<?>, ProxiedParametersFounder<?>> foundersMap = new ConcurrentHashMap<>();

    private final Class<T> entityClass;

    @Getter
    @ToString.Include
    private int limit = UNLIMITED_LIMIT;

    @Getter
    @ToString.Include
    private Map<String, SearchElement<?>> expectationMap;

    /**
     * Задает ограничение на количество результатов поиска.
     *
     * @param limit Максимальное количество результатов.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public SearchCriteria<T> limit(int limit) {
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

    /**
     * Добавляет условие "AND ==" для параметра поиска.
     *
     * @param parameterGetter Функция для получения параметра сущности.
     * @param expected        Ожидаемое значение параметра.
     * @param <E>             Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> andEquals(Function<T, E> parameterGetter, E expected) {
        return with(findParameterId(parameterGetter), SearchElement.builder()
                .binder(ConditionBinder.AND)
                .matcher(ConditionMatcher.EQUALS)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "AND ==" для параметра поиска по имени параметра.
     *
     * @param name     Имя параметра.
     * @param expected Ожидаемое значение параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public SearchCriteria<T> andEquals(String name, Object expected) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.AND)
                .matcher(ConditionMatcher.EQUALS)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "OR ==" для параметра поиска.
     *
     * @param parameterGetter Функция для получения параметра сущности.
     * @param expected        Ожидаемое значение параметра.
     * @param <E>             Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> orEquals(Function<T, E> parameterGetter, E expected) {
        return with(findParameterId(parameterGetter), SearchElement.builder()
                .binder(ConditionBinder.OR)
                .matcher(ConditionMatcher.EQUALS)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "OR ==" для параметра поиска по имени параметра.
     *
     * @param name     Имя параметра.
     * @param expected Ожидаемое значение параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public SearchCriteria<T> orEquals(String name, Object expected) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.OR)
                .matcher(ConditionMatcher.EQUALS)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "AND LIKE" для параметра поиска.
     *
     * @param parameterGetter Функция для получения параметра сущности.
     * @param expected        Ожидаемое значение параметра.
     * @param <E>             Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> andLike(Function<T, E> parameterGetter, E expected) {
        return with(findParameterId(parameterGetter), SearchElement.builder()
                .binder(ConditionBinder.AND)
                .matcher(ConditionMatcher.LIKE_IS)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "AND LIKE" для параметра поиска по имени параметра.
     *
     * @param name     Имя параметра.
     * @param expected Ожидаемое значение параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public SearchCriteria<T> andLike(String name, Object expected) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.AND)
                .matcher(ConditionMatcher.LIKE_IS)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "OR LIKE" для параметра поиска.
     *
     * @param parameterGetter Функция для получения параметра сущности.
     * @param expected        Ожидаемое значение параметра.
     * @param <E>             Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> orLike(Function<T, E> parameterGetter, E expected) {
        return with(findParameterId(parameterGetter), SearchElement.builder()
                .binder(ConditionBinder.OR)
                .matcher(ConditionMatcher.LIKE_IS)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "OR LIKE" для параметра поиска по имени параметра.
     *
     * @param name     Имя параметра.
     * @param expected Ожидаемое значение параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public SearchCriteria<T> orLike(String name, Object expected) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.OR)
                .matcher(ConditionMatcher.LIKE_IS)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "AND >" для параметра поиска.
     *
     * @param parameterGetter Функция для получения параметра сущности.
     * @param expected        Ожидаемое значение параметра.
     * @param <E>             Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> andMore(Function<T, E> parameterGetter, E expected) {
        return with(findParameterId(parameterGetter), SearchElement.builder()
                .binder(ConditionBinder.AND)
                .matcher(ConditionMatcher.MORE)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "AND >" для параметра поиска по имени параметра.
     *
     * @param name     Имя параметра.
     * @param expected Ожидаемое значение параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public SearchCriteria<T> andMore(String name, Object expected) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.AND)
                .matcher(ConditionMatcher.MORE)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "OR >" для параметра поиска.
     *
     * @param parameterGetter Функция для получения параметра сущности.
     * @param expected        Ожидаемое значение параметра.
     * @param <E>             Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> orMore(Function<T, E> parameterGetter, E expected) {
        return with(findParameterId(parameterGetter), SearchElement.builder()
                .binder(ConditionBinder.OR)
                .matcher(ConditionMatcher.MORE)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "OR >" для параметра поиска по имени параметра.
     *
     * @param name     Имя параметра.
     * @param expected Ожидаемое значение параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public SearchCriteria<T> orMore(String name, Object expected) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.OR)
                .matcher(ConditionMatcher.MORE)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "AND <" для параметра поиска.
     *
     * @param parameterGetter Функция для получения параметра сущности.
     * @param expected        Ожидаемое значение параметра.
     * @param <E>             Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> andLess(Function<T, E> parameterGetter, E expected) {
        return with(findParameterId(parameterGetter), SearchElement.builder()
                .binder(ConditionBinder.AND)
                .matcher(ConditionMatcher.LESS)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "AND <" для параметра поиска по имени параметра.
     *
     * @param name     Имя параметра.
     * @param expected Ожидаемое значение параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public SearchCriteria<T> andLess(String name, Object expected) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.AND)
                .matcher(ConditionMatcher.LESS)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "OR <" для параметра поиска.
     *
     * @param parameterGetter Функция для получения параметра сущности.
     * @param expected        Ожидаемое значение параметра.
     * @param <E>             Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> orLess(Function<T, E> parameterGetter, E expected) {
        return with(findParameterId(parameterGetter), SearchElement.builder()
                .binder(ConditionBinder.OR)
                .matcher(ConditionMatcher.LESS)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "OR <" для параметра поиска по имени параметра.
     *
     * @param name     Имя параметра.
     * @param expected Ожидаемое значение параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public SearchCriteria<T> orLess(String name, Object expected) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.OR)
                .matcher(ConditionMatcher.LESS)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "AND >=" для параметра поиска.
     *
     * @param parameterGetter Функция для получения параметра сущности.
     * @param expected        Ожидаемое значение параметра.
     * @param <E>             Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> andMoreOrEquals(Function<T, E> parameterGetter, E expected) {
        return with(findParameterId(parameterGetter), SearchElement.builder()
                .binder(ConditionBinder.AND)
                .matcher(ConditionMatcher.MORE_OR_EQUAL)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "AND >=" для параметра поиска по имени параметра.
     *
     * @param name     Имя параметра.
     * @param expected Ожидаемое значение параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public SearchCriteria<T> andMoreOrEquals(String name, Object expected) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.AND)
                .matcher(ConditionMatcher.MORE_OR_EQUAL)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "OR >=" для параметра поиска.
     *
     * @param parameterGetter Функция для получения параметра сущности.
     * @param expected        Ожидаемое значение параметра.
     * @param <E>             Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> orMoreOrEquals(Function<T, E> parameterGetter, E expected) {
        return with(findParameterId(parameterGetter), SearchElement.builder()
                .binder(ConditionBinder.OR)
                .matcher(ConditionMatcher.MORE_OR_EQUAL)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "OR >=" для параметра поиска по имени параметра.
     *
     * @param name     Имя параметра.
     * @param expected Ожидаемое значение параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public SearchCriteria<T> orMoreOrEquals(String name, Object expected) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.OR)
                .matcher(ConditionMatcher.MORE_OR_EQUAL)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "AND <=" для параметра поиска.
     *
     * @param parameterGetter Функция для получения параметра сущности.
     * @param expected        Ожидаемое значение параметра.
     * @param <E>             Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> andLessOrEquals(Function<T, E> parameterGetter, E expected) {
        return with(findParameterId(parameterGetter), SearchElement.builder()
                .binder(ConditionBinder.AND)
                .matcher(ConditionMatcher.LESS_OR_EQUAL)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "AND <=" для параметра поиска по имени параметра.
     *
     * @param name     Имя параметра.
     * @param expected Ожидаемое значение параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public SearchCriteria<T> andLessOrEquals(String name, Object expected) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.AND)
                .matcher(ConditionMatcher.LESS_OR_EQUAL)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "OR <=" для параметра поиска.
     *
     * @param parameterGetter Функция для получения параметра сущности.
     * @param expected        Ожидаемое значение параметра.
     * @param <E>             Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> orLessOrEquals(Function<T, E> parameterGetter, E expected) {
        return with(findParameterId(parameterGetter), SearchElement.builder()
                .binder(ConditionBinder.OR)
                .matcher(ConditionMatcher.LESS_OR_EQUAL)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "OR <=" для параметра поиска по имени параметра.
     *
     * @param name     Имя параметра.
     * @param expected Ожидаемое значение параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public SearchCriteria<T> orLessOrEquals(String name, Object expected) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.OR)
                .matcher(ConditionMatcher.LESS_OR_EQUAL)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "AND IS NULL" для параметра поиска.
     *
     * @param parameterGetter Функция для получения параметра сущности.
     * @param <E>             Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> andNull(Function<T, E> parameterGetter) {
        return with(findParameterId(parameterGetter), SearchElement.builder()
                .binder(ConditionBinder.AND)
                .matcher(ConditionMatcher.IS)
                .expectation(null)
                .build());
    }

    /**
     * Добавляет условие "AND IS NULL" для параметра поиска по имени параметра.
     *
     * @param name     Имя параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public SearchCriteria<T> andNull(String name) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.AND)
                .matcher(ConditionMatcher.IS)
                .expectation(null)
                .build());
    }

    /**
     * Добавляет условие "OR IS NULL" для параметра поиска.
     *
     * @param parameterGetter Функция для получения параметра сущности.
     * @param <E>             Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> orNull(Function<T, E> parameterGetter) {
        return with(findParameterId(parameterGetter), SearchElement.builder()
                .binder(ConditionBinder.OR)
                .matcher(ConditionMatcher.IS)
                .expectation(null)
                .build());
    }

    /**
     * Добавляет условие "OR IS NULL" для параметра поиска по имени параметра.
     *
     * @param name     Имя параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public SearchCriteria<T> orNull(String name) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.OR)
                .matcher(ConditionMatcher.IS)
                .expectation(null)
                .build());
    }

    /**
     * Добавляет условие "AND IN" для параметра поиска.
     *
     * @param parameterGetter Функция для получения параметра сущности.
     * @param expected        Ожидаемое значение параметра.
     * @param <E>             Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> andInside(Function<T, E> parameterGetter, E expected) {
        return with(findParameterId(parameterGetter), SearchElement.builder()
                .binder(ConditionBinder.AND)
                .matcher(ConditionMatcher.INSIDE)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "AND IN" для параметра поиска по имени параметра.
     *
     * @param name     Имя параметра.
     * @param expected Ожидаемое значение параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public SearchCriteria<T> andInside(String name, Object expected) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.AND)
                .matcher(ConditionMatcher.INSIDE)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "OR IN" для параметра поиска.
     *
     * @param parameterGetter Функция для получения параметра сущности.
     * @param expected        Ожидаемое значение параметра.
     * @param <E>             Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> orInside(Function<T, E> parameterGetter, E expected) {
        return with(findParameterId(parameterGetter), SearchElement.builder()
                .binder(ConditionBinder.OR)
                .matcher(ConditionMatcher.INSIDE)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет условие "OR IN" для параметра поиска по имени параметра.
     *
     * @param name     Имя параметра.
     * @param expected Ожидаемое значение параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public SearchCriteria<T> orInside(String name, Object expected) {
        return with(name, SearchElement.builder()
                .binder(ConditionBinder.OR)
                .matcher(ConditionMatcher.INSIDE)
                .expectation(expected)
                .build());
    }

    /**
     * Добавляет элемент поиска для параметра поиска.
     *
     * @param parameterGetter Функция для получения параметра сущности.
     * @param element         Элемент поиска.
     * @param <E>             Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> with(Function<T, E> parameterGetter, SearchElement<E> element) {
        return with(findParameterId(parameterGetter), element);
    }

    /**
     * Добавляет элемент поиска для параметра поиска по имени параметра.
     *
     * @param name    Имя параметра.
     * @param element Элемент поиска.
     * @param <E>     Тип параметра.
     * @return Текущий экземпляр {@code SearchCriteria} для цепочного вызова.
     */
    public <E> SearchCriteria<T> with(String name, SearchElement<E> element) {
        if (element.getExpectation() == null) {
            throw new NullPointerException("SearchElement#getExpectation() not filled");
        }

        if (expectationMap == null) {
            expectationMap = Collections.synchronizedMap(new HashMap<>());
        }
        expectationMap.put(name.toLowerCase(), element);
        return this;
    }

    /**
     * Проверяет, содержит ли карта ожиданий указанный параметр.
     *
     * @param id Идентификатор параметра.
     * @return {@code true}, если параметр содержится в карте ожиданий, иначе {@code false}.
     */
    public boolean isExpectationAwait(String id) {
        if (expectationMap == null) {
            return false;
        }
        return expectationMap.containsKey(id.toLowerCase());
    }

    /**
     * Получает элемент поиска для указанного параметра.
     *
     * @param id Идентификатор параметра.
     * @return Элемент поиска.
     */
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
