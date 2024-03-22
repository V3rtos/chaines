package me.moonways.bridgenet.jdbc.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.var;
import me.moonways.bridgenet.jdbc.dao.entity.Element;
import me.moonways.bridgenet.jdbc.dao.entity.Entity;
import me.moonways.bridgenet.jdbc.dao.entity.EntityTransmitter;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.core.compose.DatabaseComposer;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
public class TypedEntityDao<E> implements EntityDao<E> {

    private static final int UNLIMITED_LIMIT = -1;

    private static final DaoRequestFactory REQUEST_FACTORY = new DaoRequestFactory();
    private static final DaoRequestCaller REQUEST_CALLER = new DaoRequestCaller();

    private final DatabaseComposer composer;
    private final DatabaseConnection connection;

    private final Class<E> type;

    private Entity entityTemplate;
    private boolean marksContainerExists;

    @Override
    public void deleteMonoById(Long id) {
        log.info("deleteMonoById({}) for '{}'", id, type.getName());

        var generatedKeyLabel = getGeneratedKeyLabel();
        delete(EntityAccessCondition.createMono(generatedKeyLabel, id));
    }

    @Override
    public void deleteMono(E entityObj) {
        log.info("deleteMono({}) for '{}'", entityObj, type.getName());

        var entityAccessCondition = EntityAccessCondition.create();
        writeAccessCondition(entityAccessCondition,
                EntityTransmitter.transmitEntity(entityObj));

        delete(entityAccessCondition);
    }

    @Override
    public void deleteManyById(Long... ids) {
        log.info("deleteManyById({}) for '{}'", Arrays.toString(ids), type.getName());

        var generatedKeyLabel = getGeneratedKeyLabel();
        delete(EntityAccessCondition.createMany(generatedKeyLabel, ids));
    }

    @Override
    public void deleteMany(E... entities) {
        log.info("deleteMany({}) for '{}'", Arrays.toString(entities), type.getName());

        connection.openTransaction();

        for (E entity : entities) {
            deleteMono(entity);
        }

        connection.closeTransaction();
    }

    @Override
    public void delete(EntityAccessCondition condition) {
        log.info("delete({}) for '{}'", condition, type.getName());

        var entityTemplate = getEntityTemplate();
        markContainerExisted(entityTemplate);

        var preparedQuery = REQUEST_FACTORY.prepareDelete(composer, entityTemplate.getName(), condition);

        preparedQuery.call(connection);
    }

    @Override
    public Optional<Long> insertMono(E entityObj) {
        log.info("insertMono({}) for '{}'", entityObj, type.getName());

        var transactional = connection.ofTransactionalGet(() -> {

            var entity = EntityTransmitter.transmitEntity(entityObj);
            markContainerExisted(entity);

            var preparedQueries = REQUEST_FACTORY.prepareInserts(this, composer, entity.getName(), entity);
            return insertMono(preparedQueries);
        });

        return Optional.ofNullable(transactional);
    }

    public Long insertMono(List<CompletedQuery> preparedQueries) {
        for (int i = 0; i < preparedQueries.size(); i++) {
            Long entityId = REQUEST_CALLER.insertEntityWithId(connection, preparedQueries.get(i));

            if (i >= preparedQueries.size() - 1) {
                return entityId;
            }
        }

        return null;
    }

    @Override
    public List<Long> insertMany(E... entities) {
        log.info("insertMany({}) for '{}'", Arrays.toString(entities), type.getName());

        return connection.ofTransactionalGet(() -> {
            List<Long> entityIds = new ArrayList<>();

            for (E entityObj : entities) {
                entityIds.add(insertMono(entityObj).orElse(-1L));
            }

            return entityIds;
        });
    }

    private List<E> doFind(EntityAccessCondition condition, int limit) {
        var transactional = connection.ofTransactionalGet(() -> {
            var entityTemplate = getEntityTemplate();
            markContainerExisted(entityTemplate);

            var preparedQuery = REQUEST_FACTORY.prepareSearch(composer, entityTemplate.getName(), condition, limit);

            return preparedQuery.call(connection);
        });

        return transactional.map(responseStream -> EntityTransmitter.transmitObjectsList(responseStream, type, composer, connection))
                .get();
    }

    @Override
    public Optional<E> findMonoById(Long id) {
        log.info("findMonoById({}) for '{}'", id, type.getName());
        return findMono(EntityAccessCondition.createMono("id", id));
    }

    @Override
    public Optional<E> findMono(EntityAccessCondition condition) {
        log.info("findMono({}) for '{}'", condition, type.getName());

        List<E> list = findManyWithLimit(1, condition);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(list.get(0));
    }

    @Override
    public List<E> findManyById(Long... ids) {
        log.info("findManyById({}) for '{}'", Arrays.toString(ids), type.getName());
        return findMany(EntityAccessCondition.createMany("id", ids));
    }

    @Override
    public List<E> findMany(EntityAccessCondition condition) {
        log.info("findMany({}) for '{}'", condition, type.getName());
        return doFind(condition, UNLIMITED_LIMIT);
    }

    @Override
    public List<E> findManyWithLimit(int limit, EntityAccessCondition condition) {
        log.info("findManyWithLimit({}, {}) for '{}'", limit, condition, type.getName());
        return doFind(condition, limit);
    }

    @Override
    public List<E> findWithLimit(int limit) {
        log.info("findWithLimit({}) for '{}'", limit, type.getName());
        return doFind(null, UNLIMITED_LIMIT);
    }

    @Override
    public List<E> findAll() {
        log.info("findAll() for '{}'", type.getName());
        return findWithLimit(UNLIMITED_LIMIT);
    }

    private void markContainerExisted(Entity entityTemplate) {
        if (marksContainerExists)
            return;

        var preparedQuery = REQUEST_FACTORY.prepareContainerCreate(composer, entityTemplate);
        preparedQuery.call(connection);

        marksContainerExists = true;
    }

    private void writeAccessCondition(EntityAccessCondition condition, Entity entity) {
        var elements = entity.getElements();
        var write = 0;

        for (Element element : elements) {
            if (element.isAutoGenerated() || element.isPrimary()) {
                write++;
                condition = condition.withMono(element.getShortName(), element.getValue());
            }
        }

        if (write == 0) {
            for (Element element : elements) {
                condition = condition.withMono(element.getShortName(), element.getValue());
            }
        }
    }

    private Entity getEntityTemplate() {
        if (entityTemplate == null) {
            entityTemplate = EntityTransmitter.transmitEntityWithoutValues(type);
        }
        return entityTemplate;
    }

    private String getGeneratedKeyLabel() {
        var key = getEntityTemplate().getKey();

        var hasGeneratedKey = key != null && key.isAutoGenerated();
        return hasGeneratedKey ? key.getShortName() : EntityTransmitter.AUTO_GENERATED_LABEL_NAME;
    }
}
