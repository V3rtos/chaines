package me.moonways.bridgenet.model.parties;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PartyMembersContainer extends List<PartyMember>, Remote {

    PartyMember getMemberByName(@NotNull String name) throws RemoteException;

    PartyMember addMember(@NotNull String name) throws RemoteException;

    PartyMember removeMember(@NotNull String name) throws RemoteException;

    boolean hasMemberByName(@NotNull String name) throws RemoteException;
}
