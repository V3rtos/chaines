package me.moonways.bridgenet.endpoint.servers.players;

import me.moonways.bridgenet.api.event.InboundEventListener;
import me.moonways.bridgenet.api.event.SubscribeEvent;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.event.PlayerDisconnectEvent;

import java.rmi.RemoteException;

@InboundEventListener
public class PlayerDisconnectListener {

    @Inject
    private PlayersOnServersConnectionService playersOnServersConnectionService;

    @SubscribeEvent
    public void handle(PlayerDisconnectEvent event) throws RemoteException {
        playersOnServersConnectionService.delete(event.getPlayer().getId());
    }
}
