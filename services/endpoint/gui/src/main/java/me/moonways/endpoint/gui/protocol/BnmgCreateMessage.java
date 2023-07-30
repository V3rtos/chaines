package me.moonways.endpoint.gui.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.mtp.message.inject.ClientMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;
import me.moonways.bridgenet.mtp.transfer.provider.TransferSerializeProvider;
import me.moonways.endpoint.gui.descriptor.GuiDescriptor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ClientMessage
public class BnmgCreateMessage {

    @ByteTransfer(provider = TransferSerializeProvider.class)
    private GuiDescriptor descriptor;
}
