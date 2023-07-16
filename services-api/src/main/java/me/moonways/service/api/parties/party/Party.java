package me.moonways.service.api.parties.party;

import me.moonways.service.api.parties.participant.PartyOwner;
import me.moonways.service.api.parties.PartyMemberList;
import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;

public interface Party extends Remote {

    PartyOwner getOwner() throws RemoteException;

    PartyMemberList getPartyMemberList() throws RemoteException;

    void setOwner(@NotNull PartyOwner newOwner) throws RemoteException;

    long getTimeOfCreated(@NotNull TimeUnit unit) throws RemoteException;

    int getTotalMembersCount() throws RemoteException;
}
