package me.moonways.service.api.entities;

import me.moonways.bridgenet.rsi.service.RemoteService;
import me.moonways.service.api.entities.player.BridgenetPlayers;
import me.moonways.service.api.entities.server.BridgenetServers;

import java.rmi.RemoteException;

public interface BridgenetEntitiesService extends RemoteService {

    BridgenetPlayers getPlayers() throws RemoteException;

    BridgenetServers getServers() throws RemoteException;
}
