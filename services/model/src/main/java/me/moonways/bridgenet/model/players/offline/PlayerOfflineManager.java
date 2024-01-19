package me.moonways.bridgenet.model.players.offline;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PlayerOfflineManager extends Remote {

    OfflineEntityPlayer lookup(String playerName) throws RemoteException;

    OfflineEntityPlayer lookup(UUID playerUuid) throws RemoteException;

    OfflineDao readData(String playerName) throws RemoteException;

    OfflineDao readData(UUID playerUuid) throws RemoteException;

    CompletableFuture<Boolean> pushOfflineMessage(String playerName, String message) throws RemoteException;

    CompletableFuture<Boolean> pushOfflineMessage(UUID playerUuid, String message) throws RemoteException;
}
