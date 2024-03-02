package me.moonways.bridgenet.mtp.event;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;

@Getter
@ToString
@RequiredArgsConstructor
public class ChannelRegisteredEvent implements Event {

    private final ChannelHandlerContext context;
}
