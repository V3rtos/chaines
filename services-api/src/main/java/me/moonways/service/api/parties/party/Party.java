package me.moonways.service.api.parties.party;

import me.moonways.service.api.parties.participant.PartyOwner;
import me.moonways.service.api.parties.PartyMemberList;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public interface Party {

    PartyOwner getOwner();

    PartyMemberList getPartyMemberList();

    void setOwner(@NotNull PartyOwner newOwner);

    long getTimeOfCreated(@NotNull TimeUnit unit);

    int getTotalMembersCount();
}
