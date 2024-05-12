package me.moonways.bridgenet.jdbc.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.jdbc.core.compose.*;
import me.moonways.bridgenet.jdbc.core.compose.template.CreationTemplate;
import me.moonways.bridgenet.jdbc.core.compose.template.DeletionTemplate;
import me.moonways.bridgenet.jdbc.core.compose.template.InsertionTemplate;
import me.moonways.bridgenet.jdbc.core.compose.template.SearchTemplate;
import me.moonways.bridgenet.jdbc.core.compose.template.collection.PredicatesTemplate;
import me.moonways.bridgenet.jdbc.core.compose.template.collection.SignatureTemplate;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedQuery;
import me.moonways.bridgenet.jdbc.entity.descriptor.EntityDescriptor;
import me.moonways.bridgenet.jdbc.entity.descriptor.EntityParametersDescriptor;
import me.moonways.bridgenet.jdbc.entity.util.search.SearchElement;
import me.moonways.bridgenet.jdbc.entity.util.search.SearchMarker;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
class EntityOperationComposer {

    @Getter
    @Builder
    public static class EntityComposedOperation {

        private final int resultIndex;
        private final List<CompletedQuery> queries;

        public CompletedQuery getResultQuery() {
            return queries.get(resultIndex);
        }
    }

    private final DatabaseComposer composer;

    public EntityComposedOperation composeDelete(EntityDescriptor entity, SearchMarker<?> searchMarker) {
        return EntityComposedOperation.builder()
                .queries(Collections.singletonList(composer.useDeletionPattern()
                        .container(entity.getContainerName())
                        .predicates(composePredicatesTemplate(entity, searchMarker).combine())
                        .combine()))
                .resultIndex(0)
                .build();
    }

    public EntityComposedOperation composeSearch(EntityDescriptor entity, SearchMarker<?> searchMarker) {
        return EntityComposedOperation.builder()
                .queries(Collections.singletonList(composer.useSearchPattern()
                        .container(entity.getContainerName())
                        .limit(searchMarker.getLimit())
                        .subjects(composer.subjects()
                                .selectAll()
                                .combine())
                        .predicates(composePredicatesTemplate(entity, searchMarker).combine())
                        .combine()))
                .resultIndex(0)
                .build();
    }

    private PredicatesTemplate composePredicatesTemplate(EntityDescriptor entity, SearchMarker<?> searchMarker) {
        PredicatesTemplate predicates = composer.predicates();

        for (EntityParametersDescriptor.ParameterUnit parameterUnit : entity.getParameters().getParameterUnits()) {
            String parameterId = parameterUnit.getId();

            if (searchMarker.isExpectationAwait(parameterId)) {
                SearchElement<?> searchElement = searchMarker.getExpectation(parameterId);

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
            case MATCHES: {
                predicationAgent = predicates.ifMatches(field);
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
        return EntityComposedOperation.builder()
                .queries(Arrays.asList(
                        prepareCreateContainerQuery(entity),
                        prepareInsertQuery(entity)))
                .resultIndex(1)
                .build();
    }

    private CompletedQuery prepareInsertQuery(EntityDescriptor entity) {
        InsertionTemplate insertionTemplate = composer.useInsertionPattern()
                //.useDuplicationReduce()
                .container(entity.getContainerName());

        for (EntityParametersDescriptor.ParameterUnit parameterUnit : entity.getParameters().getParameterUnits()) {
            CombinedStructs.CombinedField combinedField = toField(parameterUnit);

            if (!parameterUnit.isAutoGenerated()) {
                insertionTemplate = insertionTemplate
                        .withValue(combinedField);
                        //.updateOnConflict(combinedField);
            }
        }

        return insertionTemplate.combine();
    }

    private CompletedQuery prepareCreateContainerQuery(EntityDescriptor entity) {
        CreationTemplate creationTemplate = composer.useCreationPattern()
                .entity(StorageType.CONTAINER)
                .name(entity.getContainerName());

        SignatureTemplate signatureTemplate = composer.signature();
        for (EntityParametersDescriptor.ParameterUnit parameterUnit : entity.getParameters().getParameterUnits()) {
            signatureTemplate = signatureTemplate.with(toStyledParameter(parameterUnit));
        }

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
