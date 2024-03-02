package me.moonways.bridgenet.jdbc.core.compose.impl.pattern.type.query;

import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.AbstractPattern;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.PatternCollectionConfigurator;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.PatternCollections;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.verification.VerificationContext;
import me.moonways.bridgenet.jdbc.core.compose.impl.pattern.verification.VerificationResult;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedGroups;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedMerges;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedPredicates;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedSubjects;
import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;
import me.moonways.bridgenet.jdbc.core.compose.template.SearchTemplate;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.*;
import org.jetbrains.annotations.NotNull;

public class SearchTemplatedPattern extends AbstractPattern implements SearchTemplate {

    public SearchTemplatedPattern() {
        super(PatternCollections.fromPattern("search.pattern"));
    }

    @Override
    public SearchTemplate limit(int limit) {
        PatternCollectionConfigurator.create("limit")
                .pushStringOnly(String.valueOf(limit))
                .adjust(getTotals());
        return this;
    }

    @Override
    public SearchTemplate container(String table) {
        PatternCollectionConfigurator.create("container")
                .pushStringOnly(table)
                .adjust(getTotals());
        return this;
    }

    @Override
    public SearchTemplate subjects(CompletedSubjects subjects) {
        PatternCollectionConfigurator.create("subjects")
                .pushSubjectsOnly(subjects)
                .adjust(getTotals());
        return this;
    }

    @Override
    public SearchTemplate predicates(CompletedPredicates predicates) {
        PatternCollectionConfigurator.create("predicates")
                .pushConditionsOnly(predicates)
                .adjust(getTotals());
        return this;
    }

    @Override
    public SearchTemplate merges(CompletedMerges merges) {
        PatternCollectionConfigurator.create("merges")
                .pushMergesOnly(merges)
                .adjust(getTotals());
        return this;
    }

    @Override
    public SearchTemplate groups(CompletedGroups groups) {
        PatternCollectionConfigurator.create("groups")
                .pushGroupsOnly(groups)
                .adjust(getTotals());
        return this;
    }

    @Override
    public SearchTemplate sort(CombinedStructs.CombinedOrderedLabel order) {
        PatternCollectionConfigurator.create("orders")
                .pushOrdersOnly(order)
                .adjust(getTotals());
        return this;
    }

    @Override
    public VerificationResult verify(@NotNull VerificationContext context) {
        return context.newTransaction(getTotals())
                .markCollectionMaxSize("limit", 1)
                .markCollectionMaxSize("container", 1)
                .markCollectionMaxSize("orders", 1)
                //.markCollectionsConflict("groups", "predicates")
                .commitVerificationResult();
    }
}
