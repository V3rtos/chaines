package me.moonways.service.bus;

import lombok.AccessLevel;
import lombok.Setter;
import me.moonways.service.api.entities.server.BridgenetServers;
//import me.moonways.service.entity.server.ServerConnectResponseType;
//import me.moonways.service.entity.server.protocol.handshake.response.HandshakeResponseMessage;


public abstract class AbstractHandshakeHandler {

    @Setter(AccessLevel.PROTECTED)
    private BridgenetServers serverManager;

    //public final void addServer(@NotNull Message message, @NotNull Server server) {
    //    if (serverManager.hasServer(server.getName())) {
    //        writeResponseHandshake(message, ServerConnectResponseType.ALREADY_CONNECTED);
    //        return;
    //    }

    //    serverManager.addServer(server);
    //    writeResponseHandshake(message, ServerConnectResponseType.ALREADY_CONNECTED);
    //}

    //public final void writeResponseHandshake(@NotNull Message message, @NotNull ServerConnectResponseType connectResponseType) {
    //    message.writeResponse(createResponseMessage(connectResponseType.getIdentifier()));
    //}

    //protected Message createResponseMessage(int connectionResponseTypeID) {
    //    return new HandshakeResponseMessage(connectionResponseTypeID);
    //}
}
