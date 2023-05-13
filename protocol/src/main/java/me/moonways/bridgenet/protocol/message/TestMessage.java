package me.moonways.bridgenet.protocol.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.protocol.transfer.ByteTransfer;

@MessageComponent(direction = ProtocolDirection.TO_SERVER, state = MessageState.REQUEST)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TestMessage extends Message {

    @ByteTransfer
    private int playerId;

    @ByteTransfer
    private String playerName;
}
