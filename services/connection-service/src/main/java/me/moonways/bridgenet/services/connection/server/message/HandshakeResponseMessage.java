package me.moonways.bridgenet.services.connection.server.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageComponent;
import me.moonways.bridgenet.protocol.message.MessageState;
import me.moonways.bridgenet.protocol.message.ProtocolDirection;

@MessageComponent(direction = ProtocolDirection.TO_CLIENT, state = MessageState.RESPONSE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HandshakeResponseMessage extends Message {

    private int result;
}
