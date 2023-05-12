package me.moonways.bridgenet.protocol.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.protocol.transfer.ByteTransfer;

@MessageComponent(direction = ProtocolDirection.SERVER)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TestMessage extends Message {

    @ByteTransfer
    private int playerId;

    @ByteTransfer
    private String playerName;
}
