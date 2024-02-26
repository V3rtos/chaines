package me.moonways.bridgenet.jdbc.core.compose.template.completed;

import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;

public interface CompletedMerges {

    CombinedStructs.CombinedMerge[] fulls(); // full

    CombinedStructs.CombinedMerge[] outsides(); // right outer

    CombinedStructs.CombinedMerge[] additions(); // left inner

    CombinedStructs.CombinedMerge[] inners(); // inner

    CombinedStructs.CombinedMerge[] unscoped(); // outer
}
