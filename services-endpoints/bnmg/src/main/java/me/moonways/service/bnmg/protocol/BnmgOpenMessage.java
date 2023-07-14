package me.moonways.service.bnmg.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageComponent;
import me.moonways.bridgenet.protocol.message.ProtocolDirection;
import me.moonways.bridgenet.protocol.transfer.ByteTransfer;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@MessageComponent(direction = ProtocolDirection.TO_CLIENT)
public class BnmgOpenMessage extends Message {

    @ByteTransfer
    private byte gui;

    @ByteTransfer
    private int playerId;
}
