package me.moonways.service.entities.server.protocol.game.arena;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.mtp.message.inject.ServerMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;
import me.moonways.bridgenet.mtp.transfer.provider.TransferUuidProvider;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ServerMessage
public class ArenaCreateMessage {

    @ByteTransfer(provider = TransferUuidProvider.class)
    private UUID arenaUUID;

    @ByteTransfer
    private String mapName;

    @ByteTransfer
    private int gameId;
    @ByteTransfer
    private int modeId;
    @ByteTransfer
    private int maxPlayers;
}
