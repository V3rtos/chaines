package me.moonways.bridgenet.model.bus.message;

import lombok.*;
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
public class Redirect {

    @ByteTransfer(provider = TransferUuidProvider.class)
    private UUID playerUUID;

    @ByteTransfer(provider = TransferUuidProvider.class)
    private UUID serverKey;

    public interface Result { }

    @Getter
    @ToString
    @ServerMessage
    @RequiredArgsConstructor
    public static class Failure implements Result {

        @ByteTransfer(provider = TransferUuidProvider.class)
        private UUID playerUUID;

        @ByteTransfer(provider = TransferUuidProvider.class)
        private UUID serverKey;
    }

    @Getter
    @ToString
    @ServerMessage
    @RequiredArgsConstructor
    public static class Success implements Result {

        @ByteTransfer(provider = TransferUuidProvider.class)
        private UUID playerUUID;

        @ByteTransfer(provider = TransferUuidProvider.class)
        private UUID serverKey;
    }
}
