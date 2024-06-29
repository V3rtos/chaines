package me.moonways.bridgenet.jdbc.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.core.ResponseRow;
import me.moonways.bridgenet.jdbc.core.ResponseStream;
import me.moonways.bridgenet.jdbc.core.compose.*;
import me.moonways.bridgenet.jdbc.core.compose.template.CreationTemplate;
import me.moonways.bridgenet.jdbc.core.compose.template.InsertionTemplate;
import me.moonways.bridgenet.jdbc.core.compose.template.collection.PredicatesTemplate;
import me.moonways.bridgenet.jdbc.core.compose.template.collection.SignatureTemplate;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedQuery;
import me.moonways.bridgenet.jdbc.core.util.result.Result;
import me.moonways.bridgenet.jdbc.entity.descriptor.EntityDescriptor;
import me.moonways.bridgenet.jdbc.entity.descriptor.EntityParametersDescriptor;
import me.moonways.bridgenet.jdbc.entity.criteria.SearchElement;
import me.moonways.bridgenet.jdbc.entity.criteria.SearchCriteria;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@RequiredArgsConstructor
class EntityOperationComposer {

    private static final Set<String> existTablesStore = new ConcurrentSkipListSet<>();
    private static final Set<UUID> connectionPreparedStore = new ConcurrentSkipListSet<>();

    static void prepareTablesStore(DatabaseConnection connection) {
        if (connectionPreparedStore.contains(connection.getId().getUniqueId())) {
            return;
        }

        Result<ResponseStream> call = connection.call("SHOW TABLES;");
        for (ResponseRow responseRow : call.get()) {
            existTablesStore.add(responseRow.field(0).getAsString().toLowerCase());
        }

        connectionPreparedStore.add(connection.getId().getUniqueId());
    }

    @Getter
    @Builder
    public static class EntityComposedOperation {

        private final int resultIndex;
        private final List<CompletedQuery> queries;

        public CompletedQuery getResultQuery() {
            if (resultIndex < 0) {
                return null;
            }
            return queries.get(resultIndex);
        }
    }

    private final DatabaseComposer composer;

    private EntityComposedOperation composeWithContainer(EntityDescriptor entity, CompletedQuery completedQuery) {
        List<CompletedQuery> completedQueryArrayList = new ArrayList<>();

        boolean contains = existTablesStore.contains(entity.getContainerName().toLowerCase());
        if (!contains) {
            completedQueryArrayList.add(prepareCreateContainerQuery(entity));
        }

        completedQueryArrayList.add(completedQuery);

        return EntityComposedOperation.builder()
                .queries(completedQueryArrayList)
                .resultIndex(contains ? 0 : 1)
                .build();
    }

    private EntityComposedOperation composeWithContainerAndChecks(EntityDescriptor entity, Supplier<CompletedQuery> completedQuery) {
        if (!existTablesStore.contains(entity.getContainerName().toLowerCase())) {
            return EntityComposedOperation.builder()
                    .queries(Collections.singletonList(prepareCreateContainerQuery(entity)))
                    .resultIndex(-1)
                    .build();
        }
        return EntityComposedOperation.builder()
                .queries(Collections.singletonList(completedQuery.get()))
                .resultIndex(0)
                .build();
    }

    public EntityComposedOperation composeDelete(EntityDescriptor entity, SearchCriteria<?> searchCriteria) {
        return composeWithContainerAndChecks(entity,
                () -> composer.useDeletionPattern()
                        .container(entity.getContainerName())
                        .predicates(composePredicatesTemplate(entity, searchCriteria).combine())
                        .combine());
    }

    public EntityComposedOperation composeSearch(EntityDescriptor entity, SearchCriteria<?> searchCriteria) {
        return composeWithContainerAndChecks(entity,
                () -> composer.useSearchPattern()
                        .container(entity.getContainerName())
                        .limit(searchCriteria.getLimit())
                        .subjects(composer.subjects()
                                .selectAll()
                                .combine())
                        .predicates(composePredicatesTemplate(entity, searchCriteria).combine())
                        .combine());
    }

    private PredicatesTemplate composePredicatesTemplate(EntityDescriptor entity, SearchCriteria<?> searchCriteria) {
        PredicatesTemplate predicates = composer.predicates();

        for (EntityParametersDescriptor.ParameterUnit parameterUnit : entity.getParameters().getParameterUnits()) {
            String parameterId = parameterUnit.getId();

            if (searchCriteria.isExpectationAwait(parameterId)) {
                SearchElement<?> searchElement = searchCriteria.getExpectation(parameterId);

                if (searchElement == null) {
                    throw new NullPointerException("expectation for " + parameterId + " from " + entity.getRootClass());
                }

                predicates = fillPredicationElement(predicates, searchElement,
                        CombinedStructs.CombinedField.builder()
                                .label(parameterId)
                                .value(searchElement.getExpectation())
                                .build());
            }
        }

        return predicates;
    }

    private PredicatesTemplate fillPredicationElement(PredicatesTemplate predicates, SearchElement<?> searchElement, CombinedStructs.CombinedField field) {
        PredicatesTemplate.PredicationAgent predicationAgent = null;
        switch (searchElement.getMatcher()) {
            case LESS: {
                predicationAgent = predicates.ifLessThen(field);
                break;
            }
            case LESS_OR_EQUAL: {
                predicationAgent = predicates.ifLessOrEqual(field);
                break;
            }
            case MORE: {
                predicationAgent = predicates.ifMoreThen(field);
                break;
            }
            case MORE_OR_EQUAL: {
                predicationAgent = predicates.ifMoreOrEqual(field);
                break;
            }
            case LIKE_IS: {
                predicationAgent = predicates.ifMatches(field);
                break;
            }
            case IS: {
                predicationAgent = predicates.isNull(CombinedStructs.CombinedLabel.builder().label(field.getLabel()).build());
                break;
            }
            case INSIDE: {
                predicationAgent = predicates.ifInside(field);
                break;
            }
            case EQUALS: {
                predicationAgent = predicates.ifEqual(field);
                break;
            }
        }
        switch (searchElement.getBinder()) {
            case OR: {
                predicates = predicationAgent.or();
                break;
            }
            case AND: {
                predicates = predicationAgent.and();
                break;
            }
            case WHERE: {
                predicates = predicationAgent.bind();
                break;
            }
        }
        return predicates;
    }

    public EntityComposedOperation composeInsert(EntityDescriptor entity) {
        return composeWithContainer(entity, prepareInsertQuery(entity));
    }

    private CompletedQuery prepareInsertQuery(EntityDescriptor entity) {
        InsertionTemplate insertionTemplate = composer.useInsertionPattern()
                .useDuplicationReduce()
                .container(entity.getContainerName());

        for (EntityParametersDescriptor.ParameterUnit parameterUnit : entity.getParameters().getParameterUnits()) {
            CombinedStructs.CombinedField combinedField = toField(parameterUnit);

            if (!parameterUnit.isAutoGenerated()) {
                insertionTemplate = insertionTemplate
                        .withValue(combinedField)
                        .updateOnConflict(combinedField);
            }
        }

        return insertionTemplate.combine();
    }

    private CompletedQuery prepareCreateContainerQuery(EntityDescriptor entity) {
        CreationTemplate creationTemplate = composer.useCreationPattern()
                .entity(StorageType.CONTAINER)
                .name(entity.getContainerName());

        SignatureTemplate signatureTemplate = composer.signature();

        for (EntityParametersDescriptor.ParameterUnit parameterUnit : new ArrayList<>(entity.getParameters().getParameterUnits())) {
            signatureTemplate = signatureTemplate.with(toStyledParameter(parameterUnit));
        }

        existTablesStore.add(entity.getContainerName().toLowerCase());
        return creationTemplate.signature(signatureTemplate.combine())
                .combine();
    }

    private CombinedStructs.CombinedStyledParameter toStyledParameter(EntityParametersDescriptor.ParameterUnit parameterUnit) {
        return CombinedStructs.CombinedStyledParameter.builder()
                .name(parameterUnit.getId())
                .style(ParameterStyle.builder()
                        .type(ParameterType.fromJavaType(parameterUnit.getType()))
                        .addons(Arrays.asList(parameterUnit.getIndexes()))
                        //.defaultValue(???)
                        .build())
                .build();
    }

    private CombinedStructs.CombinedField toField(EntityParametersDescriptor.ParameterUnit parameterUnit) {
        return CombinedStructs.field(parameterUnit.getId(), parameterUnit.getValue());
    }
}
