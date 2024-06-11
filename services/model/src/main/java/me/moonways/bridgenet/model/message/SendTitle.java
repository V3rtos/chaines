package me.moonways.bridgenet.model.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.message.persistence.ClientMessage;
import me.moonways.bridgenet.mtp.message.persistence.ServerMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;
import me.moonways.bridgenet.mtp.transfer.provider.ToUUIDProvider;

import java.util.UUID;

@Getter
@ToString
@ClientMessage
@ServerMessage
@AllArgsConstructor
@NoArgsConstructor(onConstructor_ = @Inject)
public class SendTitle {

    @ByteTransfer(provider = ToUUIDProvider.class)
    private UUID playerId;

    @ByteTransfer
    private String title;
    @ByteTransfer
    private String subtitle;

    @ByteTransfer
    private int fadeIn, stay, fadeOut;
}
