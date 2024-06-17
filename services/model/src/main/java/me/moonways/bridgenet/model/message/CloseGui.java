package me.moonways.bridgenet.model.message;

import lombok.*;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.message.persistence.ServerMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;
import me.moonways.bridgenet.mtp.transfer.provider.ToUUIDProvider;

import java.util.UUID;

@Getter
@ToString
@ServerMessage
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(onConstructor_ = @Inject)
public class CloseGui {

    @ByteTransfer(provider = ToUUIDProvider.class)
    private UUID playerId;
}
