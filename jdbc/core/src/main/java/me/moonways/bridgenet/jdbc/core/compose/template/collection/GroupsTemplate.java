package me.moonways.bridgenet.jdbc.core.compose.template.collection;

import me.moonways.bridgenet.jdbc.core.Combinable;
import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedGroups;

public interface GroupsTemplate extends Combinable<CompletedGroups> {

    GroupsTemplate with(CombinedStructs.CombinedField field);
}
