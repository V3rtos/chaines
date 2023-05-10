package me.moonways.bridgenet.service.bnmg.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageIdentifier;
import me.moonways.bridgenet.protocol.transfer.ByteTransfer;

@Getter
@AllArgsConstructor
@MessageIdentifier
public class BnmgOpenMessage extends Message {

    @ByteTransfer
    private byte gui;

    @ByteTransfer
    private int playerId;
}
