package me.moonways.bridgenet.services.connection.server.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageComponent;
import me.moonways.bridgenet.protocol.message.MessageState;
import me.moonways.bridgenet.protocol.message.ProtocolDirection;
import me.moonways.bridgenet.protocol.transfer.ByteTransfer;

@MessageComponent(direction = ProtocolDirection.TO_CLIENT, state = MessageState.RESPONSE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ArenaRegisterResponseMessage extends Message {

    @ByteTransfer
    private int result;

}
