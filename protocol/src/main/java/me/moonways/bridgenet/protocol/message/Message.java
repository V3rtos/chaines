package me.moonways.bridgenet.protocol.message;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import me.moonways.bridgenet.protocol.BridgenetChannel;
import me.moonways.bridgenet.protocol.message.exception.MessageChannelNullException;
import me.moonways.bridgenet.protocol.message.exception.MessageResponseException;
import org.jetbrains.annotations.NotNull;

@Getter
public class Message {

    @Setter
    private BridgenetChannel channel;

    @Setter
    private int messageId = 0;

    @Setter
    private int responseId = 0;

    private void validateChannelNull() {
        if (channel == null) {
            throw new MessageChannelNullException("channel is null");
        }
    }

    public void writeResponse(@NotNull Message message) {
        if (responseId != message.getResponseId()) {
            throw new MessageResponseException("wrong response id: " + responseId + " != " + message.getResponseId());
        }

        validateChannelNull();
        channel.sendMessage(message);
    }

    public boolean isResponsible() {
        return responseId != 0;
    }
}
