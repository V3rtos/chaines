package me.moonways.bridgenet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BridgenetChannel {

    private final ChannelHandlerContext channelHandlerContext;

    public Channel get() {
        return channelHandlerContext.channel();
    }
}
