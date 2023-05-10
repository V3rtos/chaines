package me.moonways.protocol.test;

import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageIdentifier;
import me.moonways.bridgenet.protocol.transfer.ByteTransfer;
import me.moonways.bridgenet.protocol.transfer.provider.TransferSerializeProvider;

@MessageIdentifier
public class TestMessage extends Message {

    @ByteTransfer
    private int playerId;

    @ByteTransfer
    private String playerName;

    @ByteTransfer(provider = TransferSerializeProvider.class)
    private Server server;
}
