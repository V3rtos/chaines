package me.moonways.bridgenet.model.event;

import lombok.*;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.model.service.parties.Party;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PartyUnregisterEvent implements Event {

    private final Party party;
}
