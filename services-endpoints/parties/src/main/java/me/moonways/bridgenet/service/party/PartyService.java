package me.moonways.bridgenet.service.party;

import lombok.Getter;
import me.moonways.bridgenet.service.inject.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public final class PartyService {

    @Getter
    private final Set<Party> registeredParties = Collections.synchronizedSet(new HashSet<>());

    private void validateNull(Party party) {
        if (party == null) {
            throw new NullPointerException("party");
        }
    }

    private void validateNull(String playerName) {
        if (playerName == null) {
            throw new NullPointerException("player name");
        }
    }

    public Party createParty(@NotNull String ownerName) {
        Party party = new Party(null, System.currentTimeMillis());
        party.setOwner(new PartyOwner(ownerName, party));

        return party;
    }

    public Party createParty(@NotNull String ownerName, @NotNull String... firstMembersNames) {
        Party createdParty = createParty(ownerName);
        PartyMemberList membersList = createdParty.getMembersList();

        for (String firstMemberName : firstMembersNames) {

            PartyMember partyMember = new PartyMember(firstMemberName, createdParty);
            membersList.add(partyMember);
        }

        return createdParty;
    }

    public void registerParty(@NotNull Party party) {
        validateNull(party);
        registeredParties.add(party);
    }

    public void unregisterParty(@NotNull Party party) {
        validateNull(party);
        registeredParties.remove(party);
    }

    public Party getRegisteredParty(@NotNull String memberName) {
        validateNull(memberName);
        return registeredParties
                .stream()
                .filter(party -> isMemberOf(party, memberName))
                .findFirst()
                .orElse(null);
    }

    public boolean isMemberOf(@NotNull Party party, @NotNull String playerName) {
        validateNull(party);
        validateNull(playerName);
        return party.getOwner().getName().equalsIgnoreCase(playerName)
                || party.getMembersList().hasMemberByName(playerName);
    }

    public boolean hasParty(@NotNull String playerName) {
        validateNull(playerName);
        return getRegisteredParty(playerName) != null;
    }
}
