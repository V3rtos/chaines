package me.moonways.bridgenet.jdbc.core.compose.template;

import me.moonways.bridgenet.jdbc.core.compose.Tabled;
import me.moonways.bridgenet.jdbc.core.compose.TemplatedQuery;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedPredicates;

public interface DeletionTemplate extends TemplatedQuery, Tabled<DeletionTemplate> {

    DeletionTemplate predicates(CompletedPredicates predicates);
}
