package me.moonways.bridgenet.jdbc.core.compose.template.collection;

import me.moonways.bridgenet.jdbc.core.Combinable;
import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedMerges;

public interface MergesTemplate extends Combinable<CompletedMerges> {

    MergesTemplate full(CombinedStructs.CombinedMerge joins);

    MergesTemplate outside(CombinedStructs.CombinedMerge joins);

    MergesTemplate additional(CombinedStructs.CombinedMerge joins);

    MergesTemplate inner(CombinedStructs.CombinedMerge joins);

    MergesTemplate unscoped(CombinedStructs.CombinedMerge joins);
}
