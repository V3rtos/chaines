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
@ServerMessage
@NoArgsConstructor
@AllArgsConstructor
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
    @ClientMessage
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result {

        @ByteTransfer(provider = TransferUuidProvider.class)
        private UUID gameId;

        @ByteTransfer(provider = TransferUuidProvider.class)
        private UUID activeId;
    }
}
