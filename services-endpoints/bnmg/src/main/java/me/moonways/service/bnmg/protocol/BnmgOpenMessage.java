package me.moonways.service.bnmg.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.mtp.message.inject.ClientMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ClientMessage
public class BnmgOpenMessage {

    @ByteTransfer
    private byte gui;

    @ByteTransfer
    private int playerId;
}
