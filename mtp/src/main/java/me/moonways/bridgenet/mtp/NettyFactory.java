package me.moonways.bridgenet.mtp;

import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.experimental.UtilityClass;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.mtp.message.codec.MessageDecoder;
import me.moonways.bridgenet.mtp.message.codec.MessageEncoder;
import me.moonways.bridgenet.mtp.pipeline.NettyChannelHandler;

@UtilityClass
public class NettyFactory {

    public EventLoopGroup createEventLoopGroup(int threads) {
        return Epoll.isAvailable() ? new EpollEventLoopGroup(threads) : new NioEventLoopGroup(threads);
    }

    public ChannelFactory<? extends ServerChannel> createServerChannelFactory() {
        return Epoll.isAvailable() ? EpollServerSocketChannel::new : NioServerSocketChannel::new;
    }

    public ChannelFactory<? extends Channel> createClientChannelFactory() {
        return Epoll.isAvailable() ? EpollSocketChannel::new : NioSocketChannel::new;
    }

    public void injectPipeline(BeansService beansService, Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        MessageDecoder decoder = pipeline.get(MessageDecoder.class);
        MessageEncoder encoder = pipeline.get(MessageEncoder.class);
        NettyChannelHandler channelHandler = pipeline.get(NettyChannelHandler.class);

        beansService.inject(decoder);
        beansService.inject(encoder);
        beansService.inject(channelHandler);
    }
}
