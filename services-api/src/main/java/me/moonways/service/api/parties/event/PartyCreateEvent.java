package me.moonways.service.api.parties.event;

import me.moonways.service.api.events.Event;
import me.moonways.service.api.parties.party.Party;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PartyCreateEvent implements Event {

    private final Party party;
}
