package me.moonways.bridgenet.model.parties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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
