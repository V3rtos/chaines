package me.moonways.bridgenet.model.bus.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.mtp.message.persistence.ClientMessage;
import me.moonways.bridgenet.mtp.message.persistence.ServerMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;
import me.moonways.bridgenet.mtp.transfer.provider.TransferEnumProvider;
import me.moonways.bridgenet.mtp.transfer.provider.TransferPropertiesProvider;
import me.moonways.bridgenet.mtp.transfer.provider.TransferUuidProvider;

import java.util.Properties;
import java.util.UUID;

@Getter
@ToString
@ServerMessage
@ClientMessage
@AllArgsConstructor
@NoArgsConstructor
public class Handshake {
    public enum Type { SERVER, PLAYER }

    @ByteTransfer(provider = TransferEnumProvider.class)
    private Type type;

    @ByteTransfer(provider = TransferPropertiesProvider.class)
    private Properties properties;

    public interface Result {
        UUID getKey();
    }

    @Getter
    @ToString
    @ClientMessage
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Success implements Result {

        @ByteTransfer(provider = TransferUuidProvider.class)
        private UUID key;
    }

    @Getter
    @ToString
    @ClientMessage
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Failure implements Result {

        @ByteTransfer(provider = TransferUuidProvider.class)
        private UUID key;
    }
}
