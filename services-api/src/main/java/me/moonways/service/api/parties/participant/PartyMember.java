package me.moonways.service.api.parties.participant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.service.api.parties.party.Party;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class PartyMember {

    private final String name;

    private final Party party;
}
