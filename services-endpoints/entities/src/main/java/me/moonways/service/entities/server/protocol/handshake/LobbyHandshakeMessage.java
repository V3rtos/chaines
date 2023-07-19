package me.moonways.service.entities.server.protocol.handshake;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.mtp.message.encryption.EncryptedMessage;
import me.moonways.bridgenet.mtp.message.inject.ClientMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EncryptedMessage
@ClientMessage
public class LobbyHandshakeMessage {

    @ByteTransfer
    private int gameId;

    @ByteTransfer
    private String gameName;

    @ByteTransfer
    private String serverName;

    @ByteTransfer
    private String host;

    @ByteTransfer
    private int port;
}
