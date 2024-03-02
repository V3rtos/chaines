package me.moonways.endpoint.games.handler;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.event.EventHandle;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.bus.message.DeleteGame;
import me.moonways.bridgenet.model.games.ActiveGame;
import me.moonways.bridgenet.model.games.Game;
import me.moonways.bridgenet.model.games.GameServer;
import me.moonways.bridgenet.model.servers.EntityServer;
import me.moonways.bridgenet.model.servers.ServerInfo;
import me.moonways.bridgenet.model.servers.event.ServerDisconnectEvent;
import me.moonways.bridgenet.mtp.MTPChannel;
import me.moonways.endpoint.games.GamesContainer;

import java.rmi.RemoteException;

@RequiredArgsConstructor
public class GamesServersDownstreamListener {

    private final GamesContainer container;

    @Inject
    private MTPChannel channel;

    @EventHandle
    public void handle(ServerDisconnectEvent event) throws RemoteException {
        EntityServer server = event.getServer();

        for (Game game : container.getCollectedGames()) {
            for (GameServer gameServer : game.getLoadedServers()) {
                ServerInfo serverInfo = gameServer.getServerInfo();

                if (serverInfo.equals(server.getServerInfo())) {

                    for (ActiveGame activeGame : gameServer.getActiveGames()) {
                        channel.sendInsideMessage(new DeleteGame(game.getUniqueId(), activeGame.getUniqueId()));
                    }
                }
            }
        }
    }
}
