package me.moonways.bridgenet.mtp.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import me.moonways.bridgenet.mtp.channel.NetworkRemoteChannel;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Optional;

@ToString
@RequiredArgsConstructor
public class InboundMessageContext<V> implements Serializable {

    private static final long serialVersionUID = 1048504507490399550L;

    @Getter
    private final long callbackID;

    @Getter
    private final V message;

    @Getter
    private final BridgenetNetworkChannel channel;
    @Getter
    private final Long timestamp;

    @Inject
    private NetworkMessagesService networkMessagesService;

    public void callback(@NotNull Object message) {
        ExportedMessage exportedMessage = networkMessagesService.export(message);
        exportedMessage.setCallbackID(callbackID);

        Optional<Boolean> pullingStateProperty = channel.getProperty(NetworkRemoteChannel.pullingStateProperty);

        if (pullingStateProperty.orElse(false)) {
            channel.pull(exportedMessage);
        } else {
            channel.send(exportedMessage);
        }
    }
}
