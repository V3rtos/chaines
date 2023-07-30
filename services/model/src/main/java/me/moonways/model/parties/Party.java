package me.moonways.model.parties;

import me.moonways.model.parties.PartyOwner;
import me.moonways.model.parties.PartyMembersContainer;
import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;

public interface Party extends Remote {

    PartyOwner getOwner() throws RemoteException;

    PartyMembersContainer getMembersContainer() throws RemoteException;

    void setOwner(@NotNull PartyOwner newOwner) throws RemoteException;

    long getTimeOfCreated(@NotNull TimeUnit unit) throws RemoteException;

    int getTotalMembersCount() throws RemoteException;
}
