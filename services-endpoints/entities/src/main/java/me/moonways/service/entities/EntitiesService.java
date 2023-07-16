package me.moonways.service.entities;

import me.moonways.service.api.entities.BridgenetEntitiesService;
import lombok.Getter;
import me.moonways.service.api.entities.player.BridgenetPlayers;
import me.moonways.service.entities.player.PlayerManager;
import me.moonways.service.api.entities.server.BridgenetServers;
import me.moonways.service.entities.server.ServerManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@Getter
public class EntitiesService extends UnicastRemoteObject implements BridgenetEntitiesService {

    private static final long serialVersionUID = 7424683335588215086L;

    private final BridgenetPlayers players = new PlayerManager();
    private final BridgenetServers servers = new ServerManager();

    public EntitiesService() throws RemoteException {
        super();
    }
}
