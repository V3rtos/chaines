package me.moonways.endpoint.language;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.ParameterAddon;
import me.moonways.bridgenet.jdbc.entity.persistence.Entity;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityColumn;

import java.util.UUID;

@ToString
@RequiredArgsConstructor
@Entity(name = "player_languages")
public class EntityLanguage {

    @Getter(onMethod_ = @EntityColumn(order = 1, id = "player_id",
            indexes = ParameterAddon.PRIMARY))
    private final UUID playerId;

    @Getter(onMethod_ = @EntityColumn(order = 2, id = "lang_id"))
    private final UUID langId;
}
