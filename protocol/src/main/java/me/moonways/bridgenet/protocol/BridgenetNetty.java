package me.moonways.bridgenet.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BridgenetNetty {

    public EventLoopGroup createEventLoopGroup(int threads) {
        return Epoll.isAvailable() ? new EpollEventLoopGroup(threads) : new NioEventLoopGroup(threads);
    }

    public ChannelFactory<? extends ServerChannel> createServerChannelFactory() {
        return Epoll.isAvailable() ? EpollServerSocketChannel::new : NioServerSocketChannel::new;
    }

    public ChannelFactory<? extends Channel> createClientChannelFactory() {
        return Epoll.isAvailable() ? EpollSocketChannel::new : NioSocketChannel::new;
    }
}
