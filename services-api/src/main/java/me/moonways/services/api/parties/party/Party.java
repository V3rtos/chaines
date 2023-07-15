package me.moonways.services.api.parties.party;

import me.moonways.services.api.parties.PartyMemberList;
import me.moonways.services.api.parties.participant.PartyOwner;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public interface Party {

    PartyOwner getOwner();

    PartyMemberList getPartyMemberList();

    void setOwner(@NotNull PartyOwner newOwner);

    long getTimeOfCreated(@NotNull TimeUnit unit);

    int getTotalMembersCount();
}
