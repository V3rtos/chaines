package me.moonways.protocol.test;

import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageComponent;
import me.moonways.bridgenet.protocol.message.ProtocolDirection;
import me.moonways.bridgenet.protocol.transfer.ByteTransfer;
import me.moonways.bridgenet.protocol.transfer.provider.TransferSerializeProvider;

@MessageComponent(direction = ProtocolDirection.SERVER)
public class TestMessage extends Message {

    @ByteTransfer
    private int playerId;

    @ByteTransfer
    private String playerName;

    @ByteTransfer(provider = TransferSerializeProvider.class)
    private Server server;
}
