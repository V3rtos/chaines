package me.moonways.bridgenet.jdbc.core.compose.impl.pattern;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.var;
import me.moonways.bridgenet.jdbc.core.BridgenetJdbcException;
import me.moonways.bridgenet.jdbc.core.compose.AggregateFunc;
import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;
import me.moonways.bridgenet.jdbc.core.compose.ConditionMatcher;
import me.moonways.bridgenet.jdbc.core.compose.MergeDirection;
import me.moonways.bridgenet.jdbc.core.compose.impl.collection.PatternCollection;
import me.moonways.bridgenet.jdbc.core.compose.impl.collection.WrappedPatternCollection;
import me.moonways.bridgenet.jdbc.core.compose.impl.collection.element.*;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.*;

import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PatternCollectionConfigurator {

    private static final CollectionElementMapper MAPPER = new CollectionElementMapper();

    public static PatternCollectionConfigurator create(String collection) {
        return new PatternCollectionConfigurator(collection);
    }

    private final String collectionName;

    private Class<?> handledType;
    private PatternCollection<?> collection;

    private void validateCollectionsType(Class<?> current) {
        if (handledType != null && !handledType.equals(current)) {
            throw new IllegalArgumentException("collection has already initialized");
        }

        if (handledType == null)
            handledType = current;
    }

    private void push(PatternCollection<?> collection) {
        if (this.collection == null) {
            this.collection = collection;
        } else {
            this.collection.addAll(collection);
        }
    }

    public void adjust(PatternCollections totals) {
        totals.add(collectionName, collection);
        flush();
    }

    private void flush() {
        handledType = null;
        collection = null;
    }

    public PatternCollectionConfigurator pushStringOnly(String value) {
        validateCollectionsType(WrappedName.class);
        push(WrappedPatternCollection.singleton(
                WrappedName.builder()
                        .value(value)
                        .build()));
        return this;
    }

    private PatternCollectionConfigurator pushSubjects(AggregateFunc function, CombinedStructs.CombinedLabel[] labels) {
        var collection = WrappedPatternCollection.multiplied();

        var array = Stream.of(labels)
                .map(label -> MAPPER.map(function, label))
                .toArray(WrappedSubject[]::new);

        collection.addAll(array);
        push(collection);

        return this;
    }

    private PatternCollectionConfigurator pushMerges(MergeDirection left_or_right, MergeDirection inner_or_outer, CombinedStructs.CombinedMerge[] merges) {
        var collection = WrappedPatternCollection.multiplied();

        var array = Stream.of(merges)
                .map(merge -> MAPPER.map(left_or_right, inner_or_outer, merge))
                .toArray(WrappedJoiner[]::new);

        collection.addAll(array);
        push(collection);

        return this;
    }

    public PatternCollectionConfigurator pushSubjectsOnly(CompletedSubjects subjects) {
        validateCollectionsType(CompletedSubjects.class);
        return pushSubjects(AggregateFunc.AVERAGE, subjects.averages())
                .pushSubjects(AggregateFunc.COUNTING, subjects.counts())
                .pushSubjects(AggregateFunc.MAXIMAL, subjects.maxes())
                .pushSubjects(AggregateFunc.MINIMAL, subjects.mines())
                .pushSubjects(AggregateFunc.SUMMING, subjects.summed())
                .pushSubjects(null, subjects.generals());
    }

    public PatternCollectionConfigurator pushConditionsOnly(CompletedPredicates predicates) {
        validateCollectionsType(CompletedPredicates.class);

        var collection = WrappedPatternCollection.multiplied();
        var firstNode = predicates.first();

        CompletedPredicates.CompletedPredicateNode next;
        while ((next = firstNode.poll()) != null) {
            if (next.field().getValue() == null && next.matcher() != ConditionMatcher.IS) {
                throw new BridgenetJdbcException("value of " + next + " is null, but matcher is not \"IS\"");
            }
            collection.add(MAPPER.map(next));
        }

        push(collection);
        return this;
    }

    public PatternCollectionConfigurator pushGroupsOnly(CompletedGroups groups) {
        validateCollectionsType(CompletedGroups.class);

        var collection = WrappedPatternCollection.multiplied();

        for (var field : groups.fields()) {
            collection.add(MAPPER.mapNamed(field));
        }

        push(collection);
        return this;
    }

    public PatternCollectionConfigurator pushMergesOnly(CompletedMerges merges) {
        validateCollectionsType(CompletedMerges.class);

        return pushMerges(MergeDirection.FULL, MergeDirection.OUTER, merges.fulls())
                .pushMerges(MergeDirection.RIGHT, MergeDirection.OUTER, merges.outsides())
                .pushMerges(MergeDirection.LEFT, MergeDirection.INNER, merges.additions())
                .pushMerges(null, MergeDirection.INNER, merges.inners())
                .pushMerges(null, MergeDirection.OUTER, merges.unscoped());
    }

    public PatternCollectionConfigurator pushOrdersOnly(CombinedStructs.CombinedOrderedLabel order) {
        validateCollectionsType(WrappedOrder.class);

        var collection = WrappedPatternCollection.multiplied();
        collection.add(MAPPER.map(order));

        push(collection);
        return this;
    }

    public PatternCollectionConfigurator pushSignatureOnly(CompletedSignature signature) {
        validateCollectionsType(CompletedSignature.class);

        var collection = WrappedPatternCollection.multiplied();

        for (var parameter : signature.parameters()) {
            collection.add(MAPPER.map(parameter));
        }

        push(collection);
        return this;
    }

    public PatternCollectionConfigurator pushEncodingOnly(Encoding encoding) {
        validateCollectionsType(Encoding.class);

        var collection = WrappedPatternCollection.singleton(encoding);
        push(collection);

        return this;
    }

    public PatternCollectionConfigurator pushValuesOnly(CombinedStructs.CombinedField[] fields) {
        validateCollectionsType(CombinedStructs.CombinedField.class);

        var collection = WrappedPatternCollection.multiplied();

        for (var field : fields) {
            collection.add(MAPPER.mapFull(field));
        }

        push(collection);
        return this;
    }

    public PatternCollectionConfigurator pushPrimaryKey(CombinedStructs.CombinedStyledParameter parameter) {
        validateCollectionsType(CompletedGroups.class);

        var collection = WrappedPatternCollection.multiplied();
        collection.add(MAPPER.mapNamed(CombinedStructs.field(parameter.getName(), null)));

        push(collection);
        return this;
    }
}
