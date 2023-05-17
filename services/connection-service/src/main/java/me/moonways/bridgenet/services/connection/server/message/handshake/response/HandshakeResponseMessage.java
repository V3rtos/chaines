package me.moonways.bridgenet.services.connection.server.message.handshake.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageComponent;
import me.moonways.bridgenet.protocol.message.MessageState;
import me.moonways.bridgenet.protocol.message.ProtocolDirection;
import me.moonways.bridgenet.protocol.transfer.ByteTransfer;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@MessageComponent(direction = ProtocolDirection.TO_CLIENT, state = MessageState.RESPONSE)
public class HandshakeResponseMessage extends Message {

    @ByteTransfer
    private int result;
}
