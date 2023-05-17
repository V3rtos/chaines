package me.moonways.bridgenet.system.connection;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.connection.server.Server;
import me.moonways.bridgenet.api.connection.server.ServerManager;
import me.moonways.bridgenet.api.connection.server.exception.ArenaAlreadyRegisteredException;
import me.moonways.bridgenet.api.connection.server.type.GameServer;
import me.moonways.bridgenet.api.connection.server.type.LobbyServer;
import me.moonways.bridgenet.api.connection.server.type.VelocityServer;
import me.moonways.bridgenet.protocol.BridgenetChannel;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageHandler;
import me.moonways.bridgenet.protocol.message.MessageTrigger;
import me.moonways.bridgenet.service.inject.Inject;
import me.moonways.bridgenet.services.connection.server.ServerConnectResponseType;
import me.moonways.bridgenet.services.connection.server.message.GameHandshakeMessage;
import me.moonways.bridgenet.services.connection.server.message.HandshakeResponseMessage;
import me.moonways.bridgenet.services.connection.server.message.LobbyHandshakeMessage;
import me.moonways.bridgenet.services.connection.server.message.VelocityHandshakeMessage;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

@MessageHandler
@Log4j2
public class ClientHandshakeChannelHandler {

    @Inject
    @Getter
    private ServerManager serverManager;

    @MessageTrigger
    public void handle(VelocityHandshakeMessage message) {
        String serverName = message.getServerName();
        String host = message.getHost();
        int port = message.getPort();

        VelocityServer velocityServer = createVelocityServer(message.getChannel(), serverName, host, port);

        addServer(message, velocityServer);

        log.info("Velocity {} successful registered", serverName);
    }

    @MessageTrigger
    public void handle(LobbyHandshakeMessage message) {
        String serverName = message.getServerName();
        String host = message.getHost();
        int port = message.getPort();

        LobbyServer lobbyServer = createLobbyServer(message.getChannel(), serverName, host, port);

        addServer(message, lobbyServer);

        log.info("Lobby {} successful registered", serverName);
    }

    @MessageTrigger
    public void handle(GameHandshakeMessage message) {
        String serverName = message.getServerName();
        String host = message.getHost();

        int port = message.getPort();

        int gameId = message.getGameId();

        GameServer gameServer = createGameServer(gameId, message.getChannel(), serverName, host, port);

        addServer(message, gameServer);

        log.info("Game {} successful registered", serverName);
    }

    private void addServer(@NotNull Message message, @NotNull Server server) {
        try {
            serverManager.addServer(server);
        } catch (ArenaAlreadyRegisteredException exception) {
            int alreadyConnectedIdentifier = ServerConnectResponseType.ALREADY_CONNECTED.getIdentifier();

            writeResponseHandshake(message, alreadyConnectedIdentifier);

            exception.printStackTrace();
        }

        int successfulConnectedIdentifier = ServerConnectResponseType.ALREADY_CONNECTED.getIdentifier();

        writeResponseHandshake(message, successfulConnectedIdentifier);
    }

    private void writeResponseHandshake(@NotNull Message message, int responseId) {
        message.writeResponse(new HandshakeResponseMessage(responseId));
    }

    private VelocityServer createVelocityServer(@NotNull BridgenetChannel channel,
                                                @NotNull String serverName,
                                                @NotNull String host,
                                                int port) {
        VelocityServer velocityServer = new VelocityServer(serverName, channel, new InetSocketAddress(host, port));

        log.info("Velocity {} successful created", serverName);
        return velocityServer;
    }

    private LobbyServer createLobbyServer(@NotNull BridgenetChannel channel,
                                          @NotNull String serverName,
                                          @NotNull String host,
                                          int port) {
        LobbyServer lobbyServer = new LobbyServer(serverName, channel, new InetSocketAddress(host, port));

        log.info("Lobby {} successful created", serverName);
        return lobbyServer;
    }

    private GameServer createGameServer(int gameId,
                                        @NotNull BridgenetChannel channel,
                                        @NotNull String serverName,
                                        @NotNull String host,
                                        int port) {
        GameServer gameServer = new GameServer(gameId, serverName, channel, new InetSocketAddress(host, port));

        log.info("Game {} successful created", serverName);
        return gameServer;
    }

}
