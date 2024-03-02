package me.moonways.bridgenet.jdbc.core.compose.template;

import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;
import me.moonways.bridgenet.jdbc.core.compose.Tabled;
import me.moonways.bridgenet.jdbc.core.compose.TemplatedQuery;

public interface InsertionTemplate extends TemplatedQuery, Tabled<InsertionTemplate> {

    InsertionTemplate withValue(CombinedStructs.CombinedField field);

    InsertionTemplate updateOnConflict(CombinedStructs.CombinedField field);

    InsertionTemplate useDuplicationReduce();
}
