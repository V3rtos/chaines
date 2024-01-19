package me.moonways.bridgenet.mtp.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.mtp.MTPMessageSender;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Getter
@ToString
@RequiredArgsConstructor
public class InputMessageContext<V> implements Serializable {

    private final V message;

    private final MTPMessageSender channel;
    private final Long timestamp;

    public void answer(@NotNull Object message) {
        channel.sendMessage(message);
    }
}
