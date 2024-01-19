package me.moonways.bridgenet.model.players.social;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface PlayerSocials extends Remote {

    SocialContainer lookup(String playerName) throws RemoteException;

    SocialContainer lookup(UUID playerUuid) throws RemoteException;

    void export(String playerName, SocialContainer container) throws RemoteException;

    void export(UUID playerUuid, SocialContainer container) throws RemoteException;

}
