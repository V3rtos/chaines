package me.moonways.bridgenet.connection;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.connection.server.Server;
import me.moonways.bridgenet.api.connection.server.ServerManager;
import me.moonways.bridgenet.api.connection.server.type.LobbyServer;
import me.moonways.bridgenet.api.connection.server.type.VelocityServer;
import me.moonways.bridgenet.protocol.BridgenetChannel;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageHandler;
import me.moonways.bridgenet.protocol.message.MessageTrigger;
import me.moonways.bridgenet.service.inject.Inject;
import me.moonways.bridgenet.services.connection.server.ServerConnectResponseType;
import me.moonways.bridgenet.services.connection.server.message.HandshakeResponseMessage;
import me.moonways.bridgenet.services.connection.server.message.LobbyHandshakeMessage;
import me.moonways.bridgenet.services.connection.server.message.VelocityHandshakeMessage;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

@MessageHandler
@Log4j2
public class BridgenetHandshakeChannelHandler {

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
    }

    @MessageTrigger
    public void handle(LobbyHandshakeMessage message) {
        String serverName = message.getServerName();
        String host = message.getHost();
        int port = message.getPort();

        LobbyServer lobbyServer = createLobbyServer(message.getChannel(), serverName, host, port);

        addServer(message, lobbyServer);
    }

    private void addServer(@NotNull Message message, @NotNull Server server) {
        try {
            serverManager.addServer(server);
        } catch (NullPointerException exception) {
            int alreadyConnectedIdentifier = ServerConnectResponseType.ALREADY_CONNECTED.getIdentifier();

            writeResponseHandshake(message, alreadyConnectedIdentifier);

            log.error("server {} already connected", server.getName());
        }

        int successfulConnectedIdentifier = ServerConnectResponseType.ALREADY_CONNECTED.getIdentifier();

        writeResponseHandshake(message, successfulConnectedIdentifier);
    }

    private void writeResponseHandshake(@NotNull Message message, int responseId) {
        message.writeResponse(new HandshakeResponseMessage(responseId));
    }

    private VelocityServer createVelocityServer(@NotNull BridgenetChannel channel, @NotNull String serverName, @NotNull String host, int port) {
        return new VelocityServer(serverName, channel, new InetSocketAddress(host, port));
    }

    private LobbyServer createLobbyServer(@NotNull BridgenetChannel channel, @NotNull String serverName, @NotNull String host, int port) {
        return new LobbyServer(serverName, channel, new InetSocketAddress(host, port));
    }
}
