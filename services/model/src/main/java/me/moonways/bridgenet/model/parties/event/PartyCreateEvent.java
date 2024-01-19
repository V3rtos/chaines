package me.moonways.bridgenet.model.parties.event;

import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.model.parties.Party;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PartyCreateEvent implements Event {

    private final Party party;
}
