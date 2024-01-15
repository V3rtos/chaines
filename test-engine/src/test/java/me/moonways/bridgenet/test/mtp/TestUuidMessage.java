package me.moonways.bridgenet.test.mtp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.mtp.message.inject.ClientMessage;
import me.moonways.bridgenet.mtp.message.inject.ServerMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;
import me.moonways.bridgenet.mtp.transfer.provider.TransferUuidProvider;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ServerMessage
@ClientMessage
public class TestUuidMessage {

    @ByteTransfer(provider = TransferUuidProvider.class)
    private UUID uuid;
}
