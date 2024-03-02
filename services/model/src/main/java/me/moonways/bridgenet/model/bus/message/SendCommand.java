package me.moonways.bridgenet.model.bus.message;

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
public class SendCommand {

    @ByteTransfer(provider = TransferUuidProvider.class)
    private UUID playerId;

    @ByteTransfer
    private String label;

    public interface Result { }

    @ClientMessage
    @ServerMessage
    public static class Success implements Result { }

    @ClientMessage
    @ServerMessage
    public static class Failure implements Result { }
}
