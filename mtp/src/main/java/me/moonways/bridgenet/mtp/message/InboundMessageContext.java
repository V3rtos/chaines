package me.moonways.bridgenet.mtp.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Getter
@ToString
@RequiredArgsConstructor
public class InboundMessageContext<V> implements Serializable {

    private static final long serialVersionUID = 1048504507490399550L;

    private final V message;

    private final BridgenetNetworkChannel channel;
    private final Long timestamp;

    public void callback(@NotNull Object message) {
        channel.send(message);
    }
}
