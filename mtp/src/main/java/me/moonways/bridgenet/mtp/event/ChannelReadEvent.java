package me.moonways.bridgenet.mtp.event;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import me.moonways.bridgenet.mtp.message.ExportedMessage;

@Getter
@ToString
@RequiredArgsConstructor
public class ChannelReadEvent implements Event {

    private final ChannelHandlerContext context;
    private final BridgenetNetworkChannel channel;

    private final ExportedMessage message;
}
