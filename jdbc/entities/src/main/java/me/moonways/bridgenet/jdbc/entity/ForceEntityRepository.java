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
import me.moonways.bridgenet.jdbc.entity.descriptor.EntityDescriptor;
import me.moonways.bridgenet.jdbc.entity.descriptor.EntityParametersDescriptor;
import me.moonways.bridgenet.jdbc.entity.util.EntityPersistenceUtil;
import me.moonways.bridgenet.jdbc.entity.util.EntityReadAndWriteUtil;
import me.moonways.bridgenet.jdbc.entity.util.search.SearchMarker;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class ForceEntityRepository<T> implements EntityRepository<T> {

    @ToString.Include
    @EqualsAndHashCode.Include
    private final Class<T> entityClass;

    private final DatabaseComposer composer;
    private final DatabaseConnection connection;

    private void doCallDelete(SearchMarker<T> searchMarker) {
        EntityOperationComposer.EntityComposedOperation operation =
                new EntityOperationComposer(composer)
                        .composeDelete(EntityReadAndWriteUtil.read(entityClass), searchMarker);
        justCall(operation);
    }

    private void justCall(EntityOperationComposer.EntityComposedOperation operation) {
        CompletedQuery completedQuery = operation.getQueries().get(operation.getResultIndex());
        completedQuery.call(connection);
    }

    @Override
    public void delete(Long id) {
        log.debug("delete({})", id);

        Optional<String> entityIDParameterName = findEntityIDParameterName();
        if (entityIDParameterName.isPresent()) {
            deleteIf(newSearchMarker().with(entityIDParameterName.get(), id));
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
            deleteIf(newSearchMarker().with(idUnitOptional.get().getId(), entityDescriptor.getId().getId()));
        } else {
            SearchMarker<T> searchMarker = newSearchMarker();
            List<EntityParametersDescriptor.ParameterUnit> parameterUnits = parameters.getParameterUnits();

            parameterUnits.stream()
                    .filter(EntityParametersDescriptor.ParameterUnit::isMaybeStatical)
                    .forEach(parameterUnit ->
                            searchMarker.with(parameterUnit.getId(), parameterUnit.getValue()));

            if (searchMarker.getExpectationMap().isEmpty()) {
                parameterUnits.forEach(parameterUnit ->
                                searchMarker.with(parameterUnit.getId(), parameterUnit.getValue()));
            }

            deleteIf(searchMarker);
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
        doCallDelete(searchMarker);
    }

    @Override
    public void update(T entity, Long id) {
        log.debug("update({}, {})", entity, id);

        Optional<String> entityIDParameterName = findEntityIDParameterName();
        if (entityIDParameterName.isPresent()) {
            updateIf(entity, newSearchMarker().with(entityIDParameterName.get(), id));
            return;
        }

        throw new DatabaseEntityException("Entity " + entityClass + " not used an @EntityId annotation");
    }

    @Override
    public void updateIf(T entity, SearchMarker<T> searchMarker) {
        log.debug("updateIf({}, {})", entity, searchMarker);
        throw new UnsupportedOperationException("jdbc-core is not supported UPDATE function");
    }

    private EntityID callAndGetEntityId(EntityOperationComposer.EntityComposedOperation operation) {
        EntityID entityId = EntityID.NOT_FOUND;

        for (CompletedQuery query : operation.getQueries()) {
            Result<ResponseStream> result = query.call(connection);

            if (Objects.equals(operation.getResultQuery(), query)) {
                entityId = EntityID.fromId(
                        Optional.ofNullable(result.get().findFirst())
                                .map(responseRow -> responseRow.field(0))
                                .map(Field::getAsLong)
                                .orElse(EntityID.NOT_AUTO_GENERATED.getId())
                );
            }
        }

        return entityId;
    }

    private EntityID doInsert(EntityDescriptor descriptor) {
        checkExternalsBeforeInsert(descriptor);

        EntityOperationComposer.EntityComposedOperation operation =
                new EntityOperationComposer(composer)
                        .composeInsert(descriptor);
        return callAndGetEntityId(operation);
    }

    @Override
    public EntityID insert(T entity) {
        log.debug("insert({})", entity);
        return connection.ofTransactionalGet(() -> doInsert(EntityReadAndWriteUtil.read(entity)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<EntityID> insertMany(T... entities) {
        log.debug("insertMany({})", Arrays.toString(entities));

        connection.openTransaction();

        List<EntityID> result = Arrays.stream(entities)
                .map(EntityReadAndWriteUtil::read)
                .map(this::doInsert)
                .collect(Collectors.toCollection(CopyOnWriteArrayList::new));

        connection.closeTransaction();
        return result;
    }

    private <V> List<V> callAndCollect(EntityOperationComposer.EntityComposedOperation operation) {
        List<V> entitisList = new ArrayList<>();

        CompletedQuery completedQuery = operation.getQueries().get(operation.getResultIndex());
        Result<ResponseStream> result = completedQuery.call(connection);

        for (ResponseRow responseRow : result.get()) {
            EntityDescriptor entityDescriptor = EntityReadAndWriteUtil.readRow(responseRow, entityClass);
            checkExternalsAfterSearch(entityDescriptor);

            Object entity = EntityReadAndWriteUtil.write(entityDescriptor);

            //noinspection unchecked
            entitisList.add((V) entity);
        }

        return entitisList;
    }

    private <V> List<V> doSearch(SearchMarker<V> searchMarker) {
        EntityOperationComposer.EntityComposedOperation operation =
                new EntityOperationComposer(composer)
                        .composeSearch(EntityReadAndWriteUtil.read(entityClass), searchMarker);
        return callAndCollect(operation);
    }

    @Override
    public Optional<T> search(Long id) {
        log.debug("search({})", id);

        Optional<String> entityIDParameterName = findEntityIDParameterName();
        if (entityIDParameterName.isPresent()) {
            return searchIf(newSearchMarker().with(entityIDParameterName.get(), id));
        }

        return Optional.empty();
    }

    @Override
    public Optional<T> searchIf(SearchMarker<T> searchMarker) {
        log.debug("searchIf({})", searchMarker);
        return doSearch(searchMarker.withLimit(1)).stream().findFirst();
    }

    @Override
    public List<T> searchMany(Long... ids) {
        log.debug("searchMany({})", Arrays.asList(ids));
        return connection.ofTransactionalGet(() -> Stream.of(ids)
                .map(this::search)
                .map(entityOptional -> entityOptional.orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

    @Override
    public List<T> searchManyIf(SearchMarker<T> searchMarker) {
        log.debug("searchManyIf({}})", searchMarker);
        return doSearch(searchMarker);
    }

    @Override
    public List<T> searchManyIf(int limit, SearchMarker<T> searchMarker) {
        log.debug("searchManyIf({}, {})", limit, searchMarker);
        return doSearch(searchMarker.withLimit(limit));
    }

    @Override
    public List<T> searchEach(int limit) {
        log.debug("searchEach({})", limit);
        return doSearch(newSearchMarker().withLimit(limit).with("*", "*"));
    }

    @Override
    public List<T> searchEach() {
        log.debug("searchEach()");
        return doSearch(newSearchMarker().with("*", "*"));
    }

    @Override
    public SearchMarker<T> newSearchMarker() {
        return new SearchMarker<>(new SearchMarker.ProxiedParametersFounder<>(entityClass));
    }

    private void checkExternalsBeforeInsert(EntityDescriptor entity) {
        List<EntityParametersDescriptor.ParameterUnit> externalUnits = entity.getParameters().getExternalUnits();

        if (!externalUnits.isEmpty()) {
            for (EntityParametersDescriptor.ParameterUnit externalUnit : externalUnits) {
                Object externalEntityObject = externalUnit.getValue();

                if (externalEntityObject != null) {
                    EntityID externalEntityID = doInsert(EntityReadAndWriteUtil.read(externalEntityObject));
                    externalUnit.setValue(externalEntityID.getId());
                } else {
                    externalUnit.setValue(EntityID.NOT_FOUND.getId());
                }

                externalUnit.setType(int.class);
            }
        }
    }

    private void checkExternalsAfterSearch(EntityDescriptor entity) {
        for (EntityParametersDescriptor.ParameterUnit externalUnit : entity.getParameters().getExternalUnits()) {

            Object externalEntity = researchExternalUnitValue(externalUnit);
            externalUnit.setValue(externalEntity);
        }
    }

    private Object researchExternalUnitValue(EntityParametersDescriptor.ParameterUnit externalUnit) {
        Object value = externalUnit.getValue();
        Class<?> type = externalUnit.getType();

        EntityRepository<?> externalEntityRepository = new ForceEntityRepository<>(type, composer, connection);

        return externalEntityRepository.search(Long.parseLong(value.toString()))
                .orElseThrow(() -> new DatabaseEntityException("External entity with ID:{" + value + "} by type " + type + " is not founded"));
    }

    private Optional<String> findEntityIDParameterName() {
        return EntityPersistenceUtil.findEntityIDWrapper(entityClass)
                .map(wrappedEntityParameter -> wrappedEntityParameter.getUnit().getId());
    }
}
