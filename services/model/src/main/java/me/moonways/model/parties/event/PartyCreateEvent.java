package me.moonways.model.parties.event;

import me.moonways.bridgenet.api.event.Event;
import me.moonways.model.parties.Party;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PartyCreateEvent implements Event {

    private final Party party;
}
