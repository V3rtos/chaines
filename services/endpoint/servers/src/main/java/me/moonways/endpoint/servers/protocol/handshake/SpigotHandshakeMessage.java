package me.moonways.endpoint.servers.protocol.handshake;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.mtp.message.inject.ClientMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ClientMessage
public class SpigotHandshakeMessage {

    @ByteTransfer
    private String serverName;

    @ByteTransfer
    private String host;

    @ByteTransfer
    private int port;
}
