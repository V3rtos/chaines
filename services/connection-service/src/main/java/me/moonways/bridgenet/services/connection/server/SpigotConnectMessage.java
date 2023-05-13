package me.moonways.bridgenet.services.connection.server;

import lombok.NoArgsConstructor;
import me.moonways.bridgenet.protocol.message.MessageComponent;
import me.moonways.bridgenet.protocol.message.MessageState;
import me.moonways.bridgenet.protocol.message.ProtocolDirection;

@MessageComponent(direction = ProtocolDirection.TO_SERVER, state = MessageState.REQUEST)
@NoArgsConstructor
public class SpigotConnectMessage extends ServerConnectMessage {

    // @ByteTransfer
    // private ...

    public SpigotConnectMessage(String serverName, String host, int port) {
        super(serverName, host, port);
    }
}
