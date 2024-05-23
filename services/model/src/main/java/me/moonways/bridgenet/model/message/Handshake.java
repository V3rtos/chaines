package me.moonways.bridgenet.model.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.Inject;
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
@ClientMessage
@AllArgsConstructor
@NoArgsConstructor(onConstructor_ = @Inject)
public class Handshake {
    public enum Type {SERVER, PLAYER}

    @ByteTransfer(provider = TransferEnumProvider.class)
    private Type type;

    @ByteTransfer(provider = TransferPropertiesProvider.class)
    private Properties properties;

    public interface Result {
        UUID getKey();

        default void onSuccess(Runnable runnable) {
            if (this instanceof Success) {
                runnable.run();
            }
        }

        default void onFailure(Runnable runnable) {
            if (this instanceof Failure) {
                runnable.run();
            }
        }
    }

    @Getter
    @ToString
    @ServerMessage
    @AllArgsConstructor
    @NoArgsConstructor(onConstructor_ = @Inject)
    public static class Success implements Result {

        @ByteTransfer(provider = TransferUuidProvider.class)
        private UUID key;
    }

    @Getter
    @ToString
    @ServerMessage
    @AllArgsConstructor
    @NoArgsConstructor(onConstructor_ = @Inject)
    public static class Failure implements Result {

        @ByteTransfer(provider = TransferUuidProvider.class)
        private UUID key;
    }
}
