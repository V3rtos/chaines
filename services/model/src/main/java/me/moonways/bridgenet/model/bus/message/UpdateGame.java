package me.moonways.bridgenet.model.bus.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.games.GameStatus;
import me.moonways.bridgenet.mtp.message.persistence.ClientMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;
import me.moonways.bridgenet.mtp.transfer.provider.TransferEnumProvider;
import me.moonways.bridgenet.mtp.transfer.provider.TransferUuidProvider;

import java.util.UUID;

@Getter
@ToString
@ClientMessage
@NoArgsConstructor(onConstructor_ = @Inject)
@AllArgsConstructor
public class UpdateGame {

    @ByteTransfer(provider = TransferUuidProvider.class)
    private UUID gameId;

    @ByteTransfer(provider = TransferUuidProvider.class)
    private UUID activeId;

    @ByteTransfer(provider = TransferEnumProvider.class)
    private GameStatus status;

    @ByteTransfer
    private int spectators;

    @ByteTransfer
    private int players;
}
