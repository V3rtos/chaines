package me.moonways.bridgenet.model.bus.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.mtp.message.persistence.ClientMessage;
import me.moonways.bridgenet.mtp.message.persistence.ServerMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;
import me.moonways.bridgenet.mtp.transfer.provider.TransferUuidProvider;

import java.util.UUID;

@Getter
@ToString
@ClientMessage
@ServerMessage
@AllArgsConstructor
@NoArgsConstructor
public class SendTitleMessage {

    @ByteTransfer(provider = TransferUuidProvider.class)
    private UUID playerId;

    @ByteTransfer
    private String title;
    @ByteTransfer
    private String subtitle;

    @ByteTransfer
    private int fadeIn, stay, fadeOut;
}
