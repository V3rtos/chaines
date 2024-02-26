package me.moonways.bridgenet.jdbc.core.compose.template.collection;

import me.moonways.bridgenet.jdbc.core.Combinable;
import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedSubjects;

public interface SubjectsTemplate extends Combinable<CompletedSubjects> {

    SubjectsTemplate selectAll();

    SubjectsTemplate select(CombinedStructs.CombinedLabel label);

    SubjectsTemplate average(CombinedStructs.CombinedLabel label);

    SubjectsTemplate count(CombinedStructs.CombinedLabel label);

    SubjectsTemplate min(CombinedStructs.CombinedLabel label);

    SubjectsTemplate max(CombinedStructs.CombinedLabel label);

    SubjectsTemplate sum(CombinedStructs.CombinedLabel label);
}
