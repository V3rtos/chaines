package me.moonways.bridgenet.service.party;

import lombok.Getter;
import me.moonways.bridgenet.injection.Component;
import me.moonways.service.api.parties.BridgenetPartiesService;
import me.moonways.service.api.parties.PartyMemberList;
import me.moonways.service.api.parties.participant.PartyMember;
import me.moonways.service.api.parties.participant.PartyOwner;
import me.moonways.service.api.parties.party.Party;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public final class BridgenetPartyServiceImpl extends UnicastRemoteObject implements BridgenetPartiesService {

    private static final long serialVersionUID = 4320252037235387938L;

    @Getter
    private final Set<Party> registeredParties = Collections.synchronizedSet(new HashSet<>());

    public BridgenetPartyServiceImpl() throws RemoteException {
        super();
    }

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

    @Override
    public PartyImpl createParty(@NotNull String ownerName) {
        PartyImpl party;

        try {
            party = new PartyImpl();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        party.setOwner(new PartyOwner(ownerName, party));
        return party;
    }

    @Override
    public Party createParty(@NotNull String ownerName, @NotNull String... firstMembersNames) {
        PartyImpl createdParty = createParty(ownerName);
        PartyMemberList membersList = createdParty.getPartyMemberList();

        for (String firstMemberName : firstMembersNames) {

            PartyMember partyMember = new PartyMember(firstMemberName, createdParty);
            membersList.add(partyMember);
        }

        return createdParty;
    }

    @Override
    public void registerParty(@NotNull Party party) {
        validateNull(party);
        registeredParties.add(party);
    }

    @Override
    public void unregisterParty(@NotNull Party party) {
        validateNull(party);
        registeredParties.remove(party);
    }

    @Override
    public Party getRegisteredParty(@NotNull String memberName) {
        validateNull(memberName);
        return registeredParties
                .stream()
                .filter(party -> isMemberOf(party, memberName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean isMemberOf(@NotNull Party party, @NotNull String playerName) {
        validateNull(party);
        validateNull(playerName);

        try {
            return party.getOwner().getName().equalsIgnoreCase(playerName)
                    || party.getPartyMemberList().hasMemberByName(playerName);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasParty(@NotNull String playerName) {
        validateNull(playerName);
        return getRegisteredParty(playerName) != null;
    }
}
