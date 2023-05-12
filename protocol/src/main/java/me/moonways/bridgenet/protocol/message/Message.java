package me.moonways.bridgenet.protocol.message;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import me.moonways.bridgenet.protocol.message.exception.MessageChannelNullException;
import me.moonways.bridgenet.protocol.message.exception.MessageResponseException;
import org.jetbrains.annotations.NotNull;

@Getter
public class Message {

    @Setter
    private Channel channel;

    @Setter
    private int messageId = 0;

    @Setter
    private int responseId = 0;

    public void writeResponse(@NotNull Message message) {
        if (responseId != message.getResponseId()) {
            throw new MessageResponseException("wrong response");
        }

        validateNull();

        channel.writeAndFlush(message);
    }

    private void validateNull() {
        if (channel == null) {
            throw new MessageChannelNullException("channel is null");
        }
    }

    public boolean isResponsible() {
        return responseId != 0;
    }
}
