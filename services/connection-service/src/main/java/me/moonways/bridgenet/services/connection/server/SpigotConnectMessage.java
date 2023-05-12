package me.moonways.bridgenet.services.connection.server;

import me.moonways.bridgenet.protocol.message.MessageIdentifier;

@MessageIdentifier
public class SpigotConnectMessage extends ServerConnectMessage {

    // @ByteTransfer
    // private ...

    public SpigotConnectMessage(String serverName, String host, int port) {
        super(serverName, host, port);
    }
}
