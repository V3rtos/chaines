package me.moonways.service.entities.server.protocol.handshake;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.mtp.message.inject.ServerMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ServerMessage
public class GameHandshakeMessage {

    @ByteTransfer
    private String serverName;
    @ByteTransfer
    private String host;

    @ByteTransfer
    private int port;
    @ByteTransfer
    private int gameId;
}
