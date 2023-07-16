package me.moonways.service.api.parties;

import me.moonways.service.api.parties.participant.PartyMember;
import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PartyMemberList extends List<PartyMember>, Remote {

    PartyMember getMemberByName(@NotNull String name) throws RemoteException;

    PartyMember addMember(@NotNull String name) throws RemoteException;

    PartyMember removeMember(@NotNull String name) throws RemoteException;

    boolean hasMemberByName(@NotNull String name) throws RemoteException;
}
