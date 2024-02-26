package me.moonways.bridgenet.jdbc.core.compose.impl.pattern.type.collection;

import me.moonways.bridgenet.jdbc.core.compose.template.collection.GroupsTemplate;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedGroups;
import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;

import java.util.ArrayList;
import java.util.List;

public class GroupsPattern implements GroupsTemplate, CompletedGroups {

    private final List<CombinedStructs.CombinedField> fields = new ArrayList<>();

    @Override
    public CompletedGroups combine() {
        return this;
    }

    @Override
    public GroupsTemplate with(CombinedStructs.CombinedField field) {
        fields.add(field);
        return this;
    }

    @Override
    public CombinedStructs.CombinedField[] fields() {
        return fields.toArray(new CombinedStructs.CombinedField[0]);
    }
}
