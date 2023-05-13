package me.moonways.bridgenet.service.bnmg.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageComponent;
import me.moonways.bridgenet.protocol.message.ProtocolDirection;
import me.moonways.bridgenet.protocol.transfer.ByteTransfer;

@Getter
@AllArgsConstructor
@MessageComponent(direction = ProtocolDirection.TO_CLIENT)
public class BnmgOpenMessage extends Message {

    @ByteTransfer
    private byte gui;

    @ByteTransfer
    private int playerId;
}
