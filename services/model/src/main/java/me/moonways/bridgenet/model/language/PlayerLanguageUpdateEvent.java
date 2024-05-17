package me.moonways.bridgenet.model.language;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;

import java.util.UUID;

@Getter
@Builder
@ToString
public class PlayerLanguageUpdateEvent implements Event {

    private final UUID playerId;
    private final Language language;
}
