package me.moonways.bridgenet.jdbc.core.compose.template.completed;

import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;
import me.moonways.bridgenet.jdbc.core.compose.ConditionBinder;
import me.moonways.bridgenet.jdbc.core.compose.ConditionMatcher;

public interface CompletedPredicates {

    CompletedPredicateNode first();

    interface CompletedPredicateNode {

        CompletedPredicateNode poll();

        CombinedStructs.CombinedField field();
        
        ConditionMatcher matcher();

        ConditionBinder binder();
    }
}
