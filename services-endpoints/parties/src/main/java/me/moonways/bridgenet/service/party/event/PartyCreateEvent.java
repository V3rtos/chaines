package me.moonways.bridgenet.service.party.event;

import lombok.RequiredArgsConstructor;
import me.moonways.service.event.Event;
import me.moonways.bridgenet.service.party.Party;

@RequiredArgsConstructor
public class PartyCreateEvent implements Event {

    private final Party party;
}
