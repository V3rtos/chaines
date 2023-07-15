package me.moonways.services.api.parties.event;

import lombok.RequiredArgsConstructor;
import me.moonways.services.api.events.event.Event;
import me.moonways.services.api.parties.party.Party;

@RequiredArgsConstructor
public class PartyCreateEvent implements Event {

    private final Party party;
}
