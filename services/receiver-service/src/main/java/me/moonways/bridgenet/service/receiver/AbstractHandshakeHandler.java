package me.moonways.bridgenet.service.receiver;

import lombok.AccessLevel;
import lombok.Setter;
import me.moonways.bridgenet.api.connection.server.Server;
import me.moonways.bridgenet.api.connection.server.ServerManager;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.services.connection.server.ServerConnectResponseType;
import me.moonways.bridgenet.services.connection.server.protocol.handshake.response.HandshakeResponseMessage;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractHandshakeHandler {

    @Setter(AccessLevel.PROTECTED)
    private ServerManager serverManager;

    public final void addServer(@NotNull Message message, @NotNull Server server) {
        if (serverManager.hasServer(server.getName())) {
            writeResponseHandshake(message, ServerConnectResponseType.ALREADY_CONNECTED);
            return;
        }

        serverManager.addServer(server);
        writeResponseHandshake(message, ServerConnectResponseType.ALREADY_CONNECTED);
    }

    public final void writeResponseHandshake(@NotNull Message message, @NotNull ServerConnectResponseType connectResponseType) {
        message.writeResponse(createResponseMessage(connectResponseType.getIdentifier()));
    }

    protected Message createResponseMessage(int connectionResponseTypeID) {
        return new HandshakeResponseMessage(connectionResponseTypeID);
    }
}
