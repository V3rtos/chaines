package me.moonways.bridgenet.model.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.message.persistence.ClientMessage;
import me.moonways.bridgenet.mtp.message.persistence.ServerMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;
import me.moonways.bridgenet.mtp.transfer.provider.ToUUIDProvider;

import java.util.UUID;

@Getter
@ToString
@ClientMessage
@ServerMessage
@AllArgsConstructor
@NoArgsConstructor(onConstructor_ = @Inject)
public class SendCommand {

    @ByteTransfer(provider = ToUUIDProvider.class)
    private UUID playerId;

    @ByteTransfer
    private String label;

    public interface Result {
    }

    @ToString
    @ClientMessage
    @ServerMessage
    @AllArgsConstructor
    @NoArgsConstructor(onConstructor_ = @Inject)
    public static class Success implements Result {

        @ByteTransfer(provider = ToUUIDProvider.class)
        private UUID playerId;
    }

    @ToString
    @ClientMessage
    @ServerMessage
    @AllArgsConstructor
    @NoArgsConstructor(onConstructor_ = @Inject)
    public static class Failure implements Result {

        @ByteTransfer(provider = ToUUIDProvider.class)
        private UUID playerId;
    }
}
