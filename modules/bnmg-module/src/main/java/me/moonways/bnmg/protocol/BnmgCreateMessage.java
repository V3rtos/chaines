package me.moonways.bnmg.protocol;

import lombok.Getter;
import me.moonways.bnmg.descriptor.GuiDescriptor;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageIdentifier;
import me.moonways.bridgenet.protocol.transfer.ByteTransfer;
import me.moonways.bridgenet.protocol.transfer.provider.TransferSerializeProvider;

@Getter
@MessageIdentifier
public class BnmgCreateMessage extends Message {

    @ByteTransfer(provider = TransferSerializeProvider.class)
    private GuiDescriptor descriptor;
}
