package me.moonways.bridgenet.jdbc.core.compose.template;

import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;
import me.moonways.bridgenet.jdbc.core.compose.Tabled;
import me.moonways.bridgenet.jdbc.core.compose.TemplatedQuery;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedGroups;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedMerges;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedPredicates;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedSubjects;

public interface SearchTemplate extends TemplatedQuery, Tabled<SearchTemplate> {

    SearchTemplate limit(int limit);

    SearchTemplate subjects(CompletedSubjects subjects);

    SearchTemplate predicates(CompletedPredicates predicates);

    SearchTemplate merges(CompletedMerges merges);

    SearchTemplate groups(CompletedGroups groups);

    SearchTemplate sort(CombinedStructs.CombinedOrderedLabel order);
}
