package me.moonways.bridgenet.service.party;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class PartyMember {

    private final String name;

    private final Party party;
}
