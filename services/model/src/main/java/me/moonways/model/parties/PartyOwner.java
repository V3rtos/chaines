package me.moonways.model.parties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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
