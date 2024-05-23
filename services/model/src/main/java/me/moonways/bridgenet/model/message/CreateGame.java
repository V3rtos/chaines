package me.moonways.bridgenet.model.message;

import lombok.*;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.message.persistence.ClientMessage;
import me.moonways.bridgenet.mtp.message.persistence.ServerMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;
import me.moonways.bridgenet.mtp.transfer.provider.TransferUuidProvider;

import java.util.UUID;

@Getter
@ToString
@ClientMessage
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(onConstructor_ = @Inject)
public class CreateGame {

    @ByteTransfer
    private String name;

    @ByteTransfer
    private String map;

    @ByteTransfer
    private int maxPlayers;

    @ByteTransfer
    private int playersInTeam;

    @Getter
    @ToString
    @ServerMessage
    @AllArgsConstructor
    @NoArgsConstructor(onConstructor_ = @Inject)
    public static class Result {

        @ByteTransfer(provider = TransferUuidProvider.class)
        private UUID gameId;

        @ByteTransfer(provider = TransferUuidProvider.class)
        private UUID activeId;
    }
}
