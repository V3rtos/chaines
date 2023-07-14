package me.moonways.service.entity.server.protocol.game.arena;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageComponent;
import me.moonways.bridgenet.protocol.message.MessageState;
import me.moonways.bridgenet.protocol.message.ProtocolDirection;

import java.util.UUID;

@MessageComponent(direction = ProtocolDirection.TO_SERVER, state = MessageState.REQUEST)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArenaCreateMessage extends Message {

    private UUID arenaUUID;
    private String mapName;

    private int gameId;
    private int modeId;

    private int maxPlayers;
}
