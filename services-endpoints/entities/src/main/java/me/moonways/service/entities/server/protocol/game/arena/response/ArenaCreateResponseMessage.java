package me.moonways.service.entities.server.protocol.game.arena.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.mtp.message.inject.ClientMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ClientMessage
public class ArenaCreateResponseMessage {

    @ByteTransfer
    private int result;
}
