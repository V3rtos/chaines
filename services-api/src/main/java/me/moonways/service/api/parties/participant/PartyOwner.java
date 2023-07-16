package me.moonways.service.api.parties.participant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.service.api.parties.party.Party;

import java.io.Serializable;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class PartyOwner extends PartyMember implements Serializable {

    private static final long serialVersionUID = -314478847826423779L;

    public PartyOwner(String name, Party party) {
        super(name, party);
    }
}
