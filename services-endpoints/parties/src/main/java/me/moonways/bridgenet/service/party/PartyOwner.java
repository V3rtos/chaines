package me.moonways.bridgenet.service.party;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class PartyOwner extends PartyMember {

    public PartyOwner(String name, Party party) {
        super(name, party);
    }
}
