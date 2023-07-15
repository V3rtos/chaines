package me.moonways.services.api.parties;

import me.moonways.bridgenet.rsi.service.RemoteService;
import me.moonways.services.api.parties.party.Party;
import org.jetbrains.annotations.NotNull;

public interface BridgenetPartiesService extends RemoteService {

    void registerParty(@NotNull Party party);

    void unregisterParty(@NotNull Party party);

    Party createParty(@NotNull String ownerName);

    Party getRegisteredParty(@NotNull String memberName);

    Party createParty(@NotNull String ownerName, @NotNull String... firstMembersNames);

    boolean isMemberOf(@NotNull Party party, @NotNull String playerName);

    boolean hasParty(@NotNull String playerName);



}
