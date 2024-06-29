package me.moonways.bridgenet.jdbc.core.compose.template.collection;

import me.moonways.bridgenet.jdbc.core.Combinable;
import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedPredicates;

public interface PredicatesTemplate extends Combinable<CompletedPredicates> {

    PredicationAgent ifEqual(CombinedStructs.CombinedField field);

    PredicationAgent ifMatches(CombinedStructs.CombinedField field);

    PredicationAgent ifMoreThen(CombinedStructs.CombinedField field);

    PredicationAgent ifLessThen(CombinedStructs.CombinedField field);

    PredicationAgent ifMoreOrEqual(CombinedStructs.CombinedField field);

    PredicationAgent ifLessOrEqual(CombinedStructs.CombinedField field);

    PredicationAgent ifInside(CombinedStructs.CombinedField field);

    PredicationAgent isNull(CombinedStructs.CombinedLabel label);

    interface PredicationAgent {

        PredicatesTemplate bind();

        PredicatesTemplate or();

        PredicatesTemplate and();
    }
}
