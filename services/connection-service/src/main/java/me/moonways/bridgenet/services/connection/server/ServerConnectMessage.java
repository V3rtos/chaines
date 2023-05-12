package me.moonways.bridgenet.services.connection.server;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.transfer.ByteTransfer;

@NoArgsConstructor
@AllArgsConstructor
public abstract class ServerConnectMessage extends Message {

    @ByteTransfer
    private String serverName;

    @ByteTransfer
    private String host;

    @ByteTransfer
    private int port;
}
