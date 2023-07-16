package me.moonways.service.api.parties.participant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.service.api.parties.party.Party;

import java.io.Serializable;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class PartyMember implements Serializable {

    private static final long serialVersionUID = 2479273430092380095L;

    private final String name;
    private final Party party;
}
