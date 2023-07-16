package me.moonways.service.api.parties.participant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.service.api.parties.party.Party;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class PartyOwner extends PartyMember {

    public PartyOwner(String name, Party party) {
        super(name, party);
    }
}
