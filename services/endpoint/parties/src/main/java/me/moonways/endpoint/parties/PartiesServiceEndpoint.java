package me.moonways.endpoint.parties;

import lombok.Getter;
import me.moonways.bridgenet.api.event.EventService;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.model.event.PartyCreateEvent;
import me.moonways.bridgenet.model.event.PartyRegisterEvent;
import me.moonways.bridgenet.model.event.PartyUnregisterEvent;
import me.moonways.bridgenet.model.service.parties.*;
import me.moonways.bridgenet.rmi.endpoint.persistance.EndpointRemoteObject;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
public final class PartiesServiceEndpoint extends EndpointRemoteObject implements PartiesServiceModel {
    private static final long serialVersionUID = 4320252037235387938L;

    private final Set<Party> registeredParties = Collections.synchronizedSet(new HashSet<>());

    @Inject
    private BeansService beansService;
    @Inject
    private EventService eventService;

    public PartiesServiceEndpoint() throws RemoteException {
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
    public PartyStub createParty(@NotNull String ownerName) {
        PartyStub party;

        try {
            party = new PartyStub();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        party.setOwner(new PartyOwner(ownerName, party));
        beansService.inject(party.getMembersContainer());

        eventService.fireEvent(
                PartyCreateEvent.builder()
                        .party(party)
                        .build());
        return party;
    }

    @Override
    public Party createParty(@NotNull String ownerName, @NotNull String... firstMembersNames) {
        PartyStub createdParty = createParty(ownerName);
        PartyMembersContainer membersList = createdParty.getMembersContainer();

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

        eventService.fireEvent(
                PartyRegisterEvent.builder()
                        .party(party)
                        .build());
    }

    @Override
    public void unregisterParty(@NotNull Party party) {
        validateNull(party);
        registeredParties.remove(party);

        eventService.fireEvent(
                PartyUnregisterEvent.builder()
                        .party(party)
                        .build());
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
                    || party.getMembersContainer().hasMemberByName(playerName);
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
