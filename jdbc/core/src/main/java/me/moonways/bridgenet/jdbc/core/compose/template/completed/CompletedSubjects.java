package me.moonways.bridgenet.jdbc.core.compose.template.completed;

import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;

public interface CompletedSubjects {

    CombinedStructs.CombinedLabel[] generals();

    CombinedStructs.CombinedLabel[] averages();

    CombinedStructs.CombinedLabel[] counts();

    CombinedStructs.CombinedLabel[] mines();

    CombinedStructs.CombinedLabel[] maxes();

    CombinedStructs.CombinedLabel[] summed();

    boolean isSelectedAll();
}
