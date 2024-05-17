package me.moonways.endpoint.language;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.ParameterAddon;
import me.moonways.bridgenet.jdbc.entity.persistence.Entity;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityParameter;

import java.util.UUID;

@Getter
@ToString
@RequiredArgsConstructor
@Entity(name = "player_languages")
public class EntityLanguage {

    @Getter(onMethod_ = @EntityParameter(order = 1, id = "player_id",
            indexes = ParameterAddon.PRIMARY))
    private final UUID playerId;

    @Getter(onMethod_ = @EntityParameter(order = 2, id = "lang_id"))
    private final UUID langId;
}
