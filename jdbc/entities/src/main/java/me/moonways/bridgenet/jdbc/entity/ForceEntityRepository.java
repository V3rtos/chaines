package me.moonways.bridgenet.jdbc.entity;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.core.Field;
import me.moonways.bridgenet.jdbc.core.ResponseRow;
import me.moonways.bridgenet.jdbc.core.ResponseStream;
import me.moonways.bridgenet.jdbc.core.compose.DatabaseComposer;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedQuery;
import me.moonways.bridgenet.jdbc.core.util.result.Result;
import me.moonways.bridgenet.jdbc.entity.adapter.TypeAdaptersControl;
import me.moonways.bridgenet.jdbc.entity.descriptor.EntityDescriptor;
import me.moonways.bridgenet.jdbc.entity.descriptor.EntityParametersDescriptor;
import me.moonways.bridgenet.jdbc.entity.util.EntityPersistenceUtil;
import me.moonways.bridgenet.jdbc.entity.util.EntityReadAndWriteUtil;
import me.moonways.bridgenet.jdbc.entity.criteria.SearchElement;
import me.moonways.bridgenet.jdbc.entity.criteria.SearchCriteria;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class ForceEntityRepository<T> implements EntityRepository<T> {
    private static final ExecutorService threadExecutor = Executors.newCachedThreadPool();

    @ToString.Include
    @EqualsAndHashCode.Include
    private final Class<T> entityClass;

    private final DatabaseComposer composer;
    private final DatabaseConnection connection;

    private final TypeAdaptersControl typeAdaptersControl = new TypeAdaptersControl();

    @Override
    public void delete(Long id) {
        log.debug("delete({})", id);

        Optional<String> entityIDParameterName = findEntityIDParameterName();
        if (entityIDParameterName.isPresent()) {
            delete(beginCriteria().andEquals(entityIDParameterName.get(), id));
            return;
        }

        throw new DatabaseEntityException("Entity " + entityClass + " not used an @EntityId annotation");
    }

    @Override
    public void delete(T entity) {
        log.debug("delete({})", entity);
        threadExecutor.submit(() -> {
            EntityDescriptor entityDescriptor = EntityReadAndWriteUtil.read(entity);
            EntityParametersDescriptor parameters = entityDescriptor.getParameters();

            Optional<EntityParametersDescriptor.ParameterUnit> idUnitOptional = parameters.findIdUnit();

            if (idUnitOptional.isPresent()) {
                doDelete(entityDescriptor,
                        beginCriteria()
                                .andEquals(idUnitOptional.get().getId(), entityDescriptor.getId().getId()));
            } else {
                SearchCriteria<T> searchCriteria = beginCriteria();
                List<EntityParametersDescriptor.ParameterUnit> parameterUnits = parameters.getParameterUnits();

                parameterUnits.stream()
                        .filter(EntityParametersDescriptor.ParameterUnit::isMaybeStatical)
                        .forEach(parameterUnit ->
                                searchCriteria.andEquals(parameterUnit.getId(), parameterUnit.getValue()));

                if (searchCriteria.getExpectationMap() == null || searchCriteria.getExpectationMap().isEmpty()) {
                    parameterUnits.forEach(parameterUnit ->
                            searchCriteria.andEquals(parameterUnit.getId(), parameterUnit.getValue()));
                }

                doDelete(entityDescriptor, searchCriteria);
            }
        });
    }

    @Override
    public void delete(Long... ids) {
        log.debug("delete({})", Arrays.toString(ids));
        connection.ofTransactional(() -> Arrays.asList(ids).forEach(this::delete));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(T... entities) {
        log.debug("delete({})", Arrays.toString(entities));
        connection.ofTransactional(() -> Arrays.asList(entities).forEach(this::delete));
    }

    @Override
    public void delete(SearchCriteria<T> searchCriteria) {
        log.debug("delete({})", searchCriteria);
        threadExecutor.submit(() -> doDelete(EntityReadAndWriteUtil.read(entityClass), searchCriteria));
    }

    @Override
    public void update(T entity, Long id) {
        log.debug("update({}, {})", entity, id);

        Optional<String> entityIDParameterName = findEntityIDParameterName();

        if (entityIDParameterName.isPresent()) {
            update(entity, beginCriteria().andEquals(entityIDParameterName.get(), id));
            return;
        }

        throw new DatabaseEntityException("Entity " + entityClass + " not used an @EntityId annotation");
    }

    @Override
    public void update(T entity, SearchCriteria<T> searchCriteria) {
        log.debug("update({}, {})", entity, searchCriteria);
        throw new UnsupportedOperationException("jdbc-core is not supported UPDATE function");
    }

    @Override
    public Mono<EntityID> insert(T entity) {
        log.debug("insert({})", entity);
        return Mono.of(CompletableFuture.supplyAsync(() ->
                connection.supplyTransactional(() -> doInsert(EntityReadAndWriteUtil.read(entity))),
                threadExecutor));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Multiple<EntityID> insert(T... entities) {
        log.debug("insert({})", Arrays.toString(entities));
        return Multiple.ofFutures(
                connection.supplyTransactional(() ->
                        Arrays.stream(entities)
                                .map(EntityReadAndWriteUtil::read)
                                .map(entityDescriptor -> CompletableFuture.supplyAsync(() -> doInsert(entityDescriptor), threadExecutor))
                                .collect(Collectors.toCollection(CopyOnWriteArrayList::new))));
    }

    @Override
    public Mono<T> search(Long id) {
        log.debug("search({})", id);

        Optional<String> entityIDParameterName = findEntityIDParameterName();
        if (entityIDParameterName.isPresent()) {
            return searchFirst(beginCriteria()
                    .andEquals(entityIDParameterName.get(), id));
        }
        return Mono.empty();
    }

    @Override
    public Mono<T> searchFirst(SearchCriteria<T> searchCriteria) {
        log.debug("searchFirst({})", searchCriteria);
        return doSearch(searchCriteria.limit(1)).first();
    }

    @Override
    public Multiple<T> search(Long... ids) {
        log.debug("search({})", Arrays.asList(ids));
        return Multiple.ofFutures(
                connection.supplyTransactional(() ->
                        Stream.of(ids)
                                .map(this::search)
                                .map(entityFuture -> entityFuture.future)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())));
    }

    @Override
    public Multiple<T> search(SearchCriteria<T> searchCriteria) {
        log.debug("search({}})", searchCriteria);
        return doSearch(searchCriteria);
    }

    @Override
    public Multiple<T> search(int limit, SearchCriteria<T> searchCriteria) {
        log.debug("search({}, {})", limit, searchCriteria);
        return doSearch(searchCriteria.limit(limit));
    }

    @Override
    public Multiple<T> searchAll(int limit) {
        log.debug("searchAll({})", limit);
        return doSearch(beginCriteria().limit(limit).andEquals("*", "*"));
    }

    @Override
    public Multiple<T> searchAll() {
        log.debug("searchAll()");
        return doSearch(beginCriteria().andEquals("*", "*"));
    }

    @Override
    public SearchCriteria<T> beginCriteria() {
        return new SearchCriteria<>(entityClass);
    }

    private void doDelete(EntityDescriptor entityDescriptor, SearchCriteria<T> searchCriteria) {
        connection.ofTransactional(() -> {
            checkExternalsBefore(entityDescriptor, searchCriteria);
            typeAdaptersControl.trySerializeValues(entityDescriptor);

            searchCriteria.getExpectationMap().forEach((name, searchElement) -> {

                SearchElement<?> serializedSearchElement = typeAdaptersControl.trySerializeSearchElement(name, entityDescriptor, searchElement);
                searchCriteria.andEquals(name, serializedSearchElement);
            });

            EntityOperationComposer.EntityComposedOperation operation =
                    new EntityOperationComposer(composer)
                            .composeDelete(entityDescriptor, searchCriteria);

            justCall(operation);
        });
    }

    private <V> Multiple<V> doSearch(SearchCriteria<V> searchCriteria) {
        return Multiple.ofFutures(connection.supplyTransactional(() -> {

            EntityDescriptor entityDescriptor = EntityReadAndWriteUtil.read(entityClass);
            checkExternalsBefore(entityDescriptor);
            checkExternalsBefore(entityDescriptor, searchCriteria);

            typeAdaptersControl.trySerializeValues(entityDescriptor);

            EntityOperationComposer.EntityComposedOperation operation =
                    new EntityOperationComposer(composer)
                            .composeSearch(entityDescriptor, searchCriteria);

            return callAndCollect(operation);
        }));
    }

    private EntityID doInsert(EntityDescriptor entityDescriptor) {
        checkExternalsBefore(entityDescriptor);
        typeAdaptersControl.trySerializeValues(entityDescriptor);

        EntityOperationComposer.EntityComposedOperation operation =
                new EntityOperationComposer(composer)
                        .composeInsert(entityDescriptor);

        return callAndGetEntityId(operation);
    }

    private void justCall(EntityOperationComposer.EntityComposedOperation operation) {
        for (CompletedQuery completedQuery : operation.getQueries()) {
            completedQuery.call(connection);
        }

        log.debug("Operation result: <NO RETURN CONTENT>");
    }

    private <V> List<CompletableFuture<V>> callAndCollect(EntityOperationComposer.EntityComposedOperation operation) {
        CompletableFuture<List<V>> queryResultFuture = null;

        for (CompletedQuery completedQuery : operation.getQueries()) {
            if (Objects.equals(operation.getResultQuery(), completedQuery)) {
                queryResultFuture = CompletableFuture.supplyAsync(() -> {

                    Result<ResponseStream> queryResult = completedQuery.call(connection);
                    List<V> entitiesList = new ArrayList<>();

                    for (ResponseRow responseRow : queryResult.get()) {
                        EntityDescriptor entityDescriptor = EntityReadAndWriteUtil.readRow(responseRow, entityClass);

                        checkExternalsAfter(entityDescriptor);
                        typeAdaptersControl.tryDeserializeValues(entityDescriptor);

                        //noinspection unchecked
                        entitiesList.add((V) EntityReadAndWriteUtil.write(entityDescriptor));
                    }
                    return entitiesList;
                });
            } else {
                completedQuery.call(connection);
            }
        }

        if (queryResultFuture != null) {
            List<CompletableFuture<V>> futures = queryResultFuture.thenApply(list ->
                    list.stream()
                            .map(CompletableFuture::completedFuture)
                            .collect(Collectors.toList())
            ).join();
            log.debug("Operation result: [{} rows]", futures.size());
            return futures;
        }

        log.debug("Operation result: [0 rows]");
        return Collections.emptyList();
    }

    private EntityID callAndGetEntityId(EntityOperationComposer.EntityComposedOperation operation) {
        EntityID entityId = EntityID.NOT_FOUND;

        for (CompletedQuery query : operation.getQueries()) {
            Result<ResponseStream> result = query.call(connection);

            if (Objects.equals(operation.getResultQuery(), query)) {
                entityId = EntityID.fromId(
                        Optional.ofNullable(result.get().findFirst())
                                .map(responseRow -> responseRow.field(0))
                                .filter(field -> field.getAsObject() instanceof Number)
                                .map(Field::getAsLong)
                                .orElse(EntityID.NOT_AUTO_GENERATED.getId())
                );
            }
        }

        log.debug("Operation result: {}", entityId);
        return entityId;
    }

    private void checkExternalsBefore(EntityDescriptor entity) {
        List<EntityParametersDescriptor.ParameterUnit> externalUnits = entity.getParameters().getExternalUnits();

        if (!externalUnits.isEmpty()) {
            for (EntityParametersDescriptor.ParameterUnit externalUnit : externalUnits) {
                Object externalEntityObject = externalUnit.getValue();

                if (externalEntityObject instanceof Number) {
                    continue;
                }
                if (externalEntityObject != null) {
                    EntityID externalId = doInsert(EntityReadAndWriteUtil.read(externalEntityObject));
                    externalUnit.setValue(externalId.getId());
                } else {
                    externalUnit.setValue(EntityID.NOT_FOUND.getId());
                }
                externalUnit.setType(int.class);
            }
        }
    }

    private void checkExternalsBefore(EntityDescriptor descriptor, SearchCriteria<?> searchCriteria) {
        searchCriteria.getExpectationMap().forEach((name, searchElement) -> {
            Optional<EntityParametersDescriptor.ParameterUnit> parameterUnitOptional
                    = descriptor.getParameters().find(name);

            parameterUnitOptional
                    .filter(EntityParametersDescriptor.ParameterUnit::isExternal)
                    .ifPresent(externalUnit -> {

                        if (searchElement.getExpectation() instanceof Number) {
                            return;
                        }

                        Object externalEntityObject = externalUnit.getValue();

                        if (externalEntityObject instanceof Number) {
                            searchCriteria.andEquals(name, externalEntityObject);
                            return;
                        }

                        if (externalEntityObject == null) {
                            externalUnit.setValue(EntityID.NOT_FOUND.getId());
                        }

                        externalUnit.setType(int.class);

                        searchCriteria.andEquals(name, externalUnit.getValue());
                    });
        });
    }

    private void checkExternalsAfter(EntityDescriptor entity) {
        for (EntityParametersDescriptor.ParameterUnit externalUnit : entity.getParameters().getExternalUnits()) {

            Object externalEntity = researchExternalUnitValue(externalUnit);

            externalUnit.setValue(externalEntity);
            externalUnit.setType(externalEntity.getClass());
        }
    }

    private Object researchExternalUnitValue(EntityParametersDescriptor.ParameterUnit externalUnit) {
        Object value = externalUnit.getValue();
        Class<?> type = externalUnit.getType();

        EntityRepository<?> externalEntityRepository = new ForceEntityRepository<>(type, composer, connection);

        return externalEntityRepository.search(Long.parseLong(value.toString())).block(); // todo - лучше не надо так тут конечно...
    }

    private Optional<String> findEntityIDParameterName() {
        return EntityPersistenceUtil.findEntityIDWrapper(entityClass)
                .map(wrappedEntityParameter -> wrappedEntityParameter.getUnit().getId());
    }
}
