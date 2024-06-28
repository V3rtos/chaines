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
import me.moonways.bridgenet.jdbc.entity.util.search.SearchElement;
import me.moonways.bridgenet.jdbc.entity.util.search.SearchMarker;

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

    private void doDelete(EntityDescriptor entityDescriptor, SearchMarker<T> searchMarker) {
        threadExecutor.submit(() -> {
            connection.openTransaction();

            checkExternalsBefore(entityDescriptor, searchMarker);
            typeAdaptersControl.trySerializeValues(entityDescriptor);

            searchMarker.getExpectationMap().forEach((name, searchElement) -> {

                SearchElement<?> serializedSearchElement = typeAdaptersControl.trySerializeSearchElement(name, entityDescriptor, searchElement);
                searchMarker.and(name, serializedSearchElement);
            });

            EntityOperationComposer.EntityComposedOperation operation =
                    new EntityOperationComposer(composer)
                            .composeDelete(entityDescriptor, searchMarker);

            justCall(operation);
            connection.closeTransaction();
        });
    }

    private <V> ListFuture<V> doSearch(SearchMarker<V> searchMarker) {
        EntityDescriptor entityDescriptor = EntityReadAndWriteUtil.read(entityClass);
        checkExternalsBefore(entityDescriptor);
        checkExternalsBefore(entityDescriptor, searchMarker);

        typeAdaptersControl.trySerializeValues(entityDescriptor);

        EntityOperationComposer.EntityComposedOperation operation =
                new EntityOperationComposer(composer)
                        .composeSearch(entityDescriptor, searchMarker);

        return callAndCollect(operation);
    }

    private CompletableFuture<EntityID> doInsert(EntityDescriptor entityDescriptor) {
        return CompletableFuture.supplyAsync(() -> {
            connection.openTransaction();

            checkExternalsBefore(entityDescriptor);
            typeAdaptersControl.trySerializeValues(entityDescriptor);

            EntityOperationComposer.EntityComposedOperation operation =
                    new EntityOperationComposer(composer)
                            .composeInsert(entityDescriptor);

            EntityID entityID = callAndGetEntityId(operation);
            connection.closeTransaction();
            return entityID;

        }, threadExecutor);
    }

    @Override
    public void delete(Long id) {
        log.debug("delete({})", id);

        Optional<String> entityIDParameterName = findEntityIDParameterName();
        if (entityIDParameterName.isPresent()) {
            deleteIf(newSearchMarker().and(entityIDParameterName.get(), id));
            return;
        }

        throw new DatabaseEntityException("Entity " + entityClass + " not used an @EntityId annotation");
    }

    @Override
    public void delete(T entity) {
        log.debug("delete({})", entity);

        EntityDescriptor entityDescriptor = EntityReadAndWriteUtil.read(entity);
        EntityParametersDescriptor parameters = entityDescriptor.getParameters();

        Optional<EntityParametersDescriptor.ParameterUnit> idUnitOptional = parameters.findIdUnit();

        if (idUnitOptional.isPresent()) {
            doDelete(entityDescriptor,
                    newSearchMarker()
                            .and(idUnitOptional.get().getId(), entityDescriptor.getId().getId()));
        } else {
            SearchMarker<T> searchMarker = newSearchMarker();
            List<EntityParametersDescriptor.ParameterUnit> parameterUnits = parameters.getParameterUnits();

            parameterUnits.stream()
                    .filter(EntityParametersDescriptor.ParameterUnit::isMaybeStatical)
                    .forEach(parameterUnit ->
                            searchMarker.and(parameterUnit.getId(), parameterUnit.getValue()));

            if (searchMarker.getExpectationMap() == null || searchMarker.getExpectationMap().isEmpty()) {
                parameterUnits.forEach(parameterUnit ->
                        searchMarker.and(parameterUnit.getId(), parameterUnit.getValue()));
            }

            doDelete(entityDescriptor, searchMarker);
        }
    }

    @Override
    public void deleteMany(Long... ids) {
        log.debug("deleteMany({})", Arrays.toString(ids));

        connection.ofTransactionalGet(() -> {

            Arrays.asList(ids).forEach(this::delete);
            return Void.TYPE;
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deleteMany(T... entities) {
        log.debug("deleteMany({})", Arrays.toString(entities));

        connection.ofTransactionalGet(() -> {

            Arrays.asList(entities).forEach(this::delete);
            return Void.TYPE;
        });
    }

    @Override
    public void deleteIf(SearchMarker<T> searchMarker) {
        log.debug("deleteIf({})", searchMarker);
        doDelete(EntityReadAndWriteUtil.read(entityClass), searchMarker);
    }

    @Override
    public void update(T entity, Long id) {
        log.debug("update({}, {})", entity, id);

        Optional<String> entityIDParameterName = findEntityIDParameterName();

        if (entityIDParameterName.isPresent()) {
            updateIf(entity, newSearchMarker().and(entityIDParameterName.get(), id));
            return;
        }

        throw new DatabaseEntityException("Entity " + entityClass + " not used an @EntityId annotation");
    }

    @Override
    public void updateIf(T entity, SearchMarker<T> searchMarker) {
        log.debug("updateIf({}, {})", entity, searchMarker);
        throw new UnsupportedOperationException("jdbc-core is not supported UPDATE function");
    }

    @Override
    public SingleFuture<EntityID> insert(T entity) {
        log.debug("insert({})", entity);
        return SingleFuture.of(doInsert(EntityReadAndWriteUtil.read(entity)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public ListFuture<EntityID> insertMany(T... entities) {
        log.debug("insertMany({})", Arrays.toString(entities));
        return ListFuture.ofFutures(
                connection.ofTransactionalGet(() ->
                        Arrays.stream(entities)
                                .map(EntityReadAndWriteUtil::read)
                                .map(this::doInsert)
                                .collect(Collectors.toCollection(CopyOnWriteArrayList::new))));
    }

    @Override
    public SingleFuture<T> search(Long id) {
        log.debug("search({})", id);

        Optional<String> entityIDParameterName = findEntityIDParameterName();
        if (entityIDParameterName.isPresent()) {
            return searchIf(newSearchMarker()
                    .and(entityIDParameterName.get(), id));
        }
        return SingleFuture.empty();
    }

    @Override
    public SingleFuture<T> searchIf(SearchMarker<T> searchMarker) {
        log.debug("searchIf({})", searchMarker);
        return doSearch(searchMarker.limit(1)).first();
    }

    @Override
    public ListFuture<T> searchMany(Long... ids) {
        log.debug("searchMany({})", Arrays.asList(ids));
        return ListFuture.ofFutures(
                connection.ofTransactionalGet(() ->
                        Stream.of(ids)
                                .map(this::search)
                                .map(entityFuture -> entityFuture.future)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())));
    }

    @Override
    public ListFuture<T> searchManyIf(SearchMarker<T> searchMarker) {
        log.debug("searchManyIf({}})", searchMarker);
        return doSearch(searchMarker);
    }

    @Override
    public ListFuture<T> searchManyIf(int limit, SearchMarker<T> searchMarker) {
        log.debug("searchManyIf({}, {})", limit, searchMarker);
        return doSearch(searchMarker.limit(limit));
    }

    @Override
    public ListFuture<T> searchEach(int limit) {
        log.debug("searchEach({})", limit);
        return doSearch(newSearchMarker().limit(limit).and("*", "*"));
    }

    @Override
    public ListFuture<T> searchEach() {
        log.debug("searchEach()");
        return doSearch(newSearchMarker().and("*", "*"));
    }

    @Override
    public SearchMarker<T> newSearchMarker() {
        return new SearchMarker<>(entityClass);
    }

    private void justCall(EntityOperationComposer.EntityComposedOperation operation) {
        for (CompletedQuery completedQuery : operation.getQueries()) {
            completedQuery.call(connection);
        }

        log.debug("Operation result: <NO RETURN CONTENT>");
    }

    private <V> ListFuture<V> callAndCollect(EntityOperationComposer.EntityComposedOperation operation) {
        connection.openTransaction();
        ListFuture<V> listFutureResult = ListFuture.empty();

        for (CompletedQuery completedQuery : operation.getQueries()) {
            Result<ResponseStream> result = completedQuery.call(connection);

            if (Objects.equals(operation.getResultQuery(), completedQuery)) {
                listFutureResult.addAll(
                        ListFuture.of(result.get(), ResponseRow.class)
                                .mapEach(responseRow -> {
                                    EntityDescriptor entityDescriptor = EntityReadAndWriteUtil.readRow(responseRow, entityClass);

                                    checkExternalsAfter(entityDescriptor);
                                    typeAdaptersControl.tryDeserializeValues(entityDescriptor);

                                    //noinspection unchecked
                                    return (V)EntityReadAndWriteUtil.write(entityDescriptor);
                                }));
            }
        }

        if (listFutureResult.isEmpty()) {
            connection.closeTransaction();
        }

        log.debug("Operation result: [{} elements]", listFutureResult.size());
        return listFutureResult.subscribeLast(connection::closeTransaction);
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
                    doInsert(EntityReadAndWriteUtil.read(externalEntityObject))
                            .thenAccept(externalEntityID -> externalUnit.setValue(externalEntityID.getId()));
                } else {
                    externalUnit.setValue(EntityID.NOT_FOUND.getId());
                }

                externalUnit.setType(int.class);
            }
        }
    }

    private void checkExternalsBefore(EntityDescriptor descriptor, SearchMarker<?> searchMarker) {
        searchMarker.getExpectationMap().forEach((name, searchElement) -> {
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
                            searchMarker.and(name, externalEntityObject);
                            return;
                        }

                        if (externalEntityObject == null) {
                            externalUnit.setValue(EntityID.NOT_FOUND.getId());
                        }

                        externalUnit.setType(int.class);

                        searchMarker.and(name, externalUnit.getValue());
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
