package me.moonways.bridgenet.service.bnmg.protocol;

import lombok.Getter;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageIdentifier;
import me.moonways.bridgenet.protocol.transfer.ByteTransfer;
import me.moonways.bridgenet.protocol.transfer.provider.TransferSerializeProvider;
import me.moonways.bridgenet.service.bnmg.descriptor.GuiDescriptor;

@Getter
@MessageIdentifier
public class BnmgCreateMessage extends Message {

    @ByteTransfer(provider = TransferSerializeProvider.class)
    private GuiDescriptor descriptor;
}
