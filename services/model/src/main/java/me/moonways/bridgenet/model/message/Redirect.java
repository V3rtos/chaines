package me.moonways.bridgenet.model.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.Inject;
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
@NoArgsConstructor(onConstructor_ = @Inject)
public class Redirect {

    @ByteTransfer(provider = TransferUuidProvider.class)
    private UUID playerUUID;

    @ByteTransfer(provider = TransferUuidProvider.class)
    private UUID serverKey;

    public interface Result { }

    @Getter
    @ToString
    @ServerMessage
    @AllArgsConstructor
    @NoArgsConstructor(onConstructor_ = @Inject)
    public static class Failure implements Result {

        @ByteTransfer(provider = TransferUuidProvider.class)
        private UUID playerUUID;

        @ByteTransfer(provider = TransferUuidProvider.class)
        private UUID serverKey;
    }

    @Getter
    @ToString
    @ServerMessage
    @AllArgsConstructor
    @NoArgsConstructor(onConstructor_ = @Inject)
    public static class Success implements Result {

        @ByteTransfer(provider = TransferUuidProvider.class)
        private UUID playerUUID;

        @ByteTransfer(provider = TransferUuidProvider.class)
        private UUID serverKey;
    }
}
