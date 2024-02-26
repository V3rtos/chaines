package me.moonways.bridgenet.jdbc.core.compose.template;

import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;
import me.moonways.bridgenet.jdbc.core.compose.Tabled;
import me.moonways.bridgenet.jdbc.core.compose.TemplatedQuery;

public interface RestorationTemplate extends TemplatedQuery, Tabled<RestorationTemplate> {

    RestorationTemplate update(CombinedStructs.CombinedStyledParameter parameter);

    RestorationTemplate add(CombinedStructs.CombinedStyledParameter parameter);

    RestorationTemplate delete(CombinedStructs.CombinedField field);

    RestorationTemplate rename(CombinedStructs.CombinedRenaming renaming);
}
