package me.moonways.endpoint.bus;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.mtp.message.persistence.MessageHandler;
/*
import me.moonways.bridgenet.service.game.GameService;
import me.moonways.services.api.games.data.Game;
import me.moonways.bridgenet.service.inject.InitMethod;
import me.moonways.bridgenet.service.inject.Inject;
import me.moonways.service.entity.server.protocol.handshake.GameHandshakeMessage;
import me.moonways.service.entity.server.protocol.handshake.LobbyHandshakeMessage;
*/


@MessageHandler
@Log4j2
public class GameHandshakeHandler extends AbstractHandshakeHandler {

    //@Inject
    //private GameService gameService;

    //@Inject
    //private ServerManager serverManager;

    //@InitMethod
    //private void init() {
    //    super.setServerManager(serverManager);
    //}

    //@MessageTrigger
    //public void handle(LobbyHandshakeMessage message) {
    //    String serverName = message.getServerName();
    //    String gameName = message.getGameName();
    //    String host = message.getHost();

    //    int port = message.getPort();
    //    int gameId = message.getGameId();

    //    Game game = Game.create(gameId, gameName);

    //    LobbyServer lobbyServer = createLobbyServer(message.getChannel(), game, serverName, host, port);
    //    addServer(message, lobbyServer);

    //    log.info("Lobby {} successful registered", serverName);
    //}

    //@MessageTrigger
    //public void handle(GameHandshakeMessage message) {
    //    String serverName = message.getServerName();
    //    String host = message.getHost();

    //    int port = message.getPort();
    //    int gameId = message.getGameId();

    //    GameServer gameServer = createGameServer(message.getChannel(), serverName, host, port, gameId);
    //    addServer(message, gameServer);

    //    log.info("Game {} successful registered", serverName);
    //}

    //private LobbyServer createLobbyServer(BridgenetChannel channel, Game game, String serverName,
    //                                      String host, int port) {

    //    InetSocketAddress serverAddress = new InetSocketAddress(host, port);
    //    return new LobbyServer(game, serverName, channel, serverAddress);
    //}

    //private GameServer createGameServer(BridgenetChannel channel, String serverName, String host,
    //                                    int port, int gameId) {

    //    Game registeredGame = gameService.getRegisteredGame(gameId);

    //    InetSocketAddress serverAddress = new InetSocketAddress(host, port);
    //    return new GameServer(registeredGame, serverName, channel, serverAddress);
    //}
}
