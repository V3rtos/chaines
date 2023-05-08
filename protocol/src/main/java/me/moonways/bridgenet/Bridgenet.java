package me.moonways.bridgenet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.pipeline.BridgenetConfiguration;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public class Bridgenet {

    private final MessageRegistryContainer messageRegistryContainer = new MessageRegistryContainer();

    private final String host;
    private final int port;

    public static Bridgenet of(String host, int port) {
        return new Bridgenet(host, port);
    }

    public static class ServerBuilder {

        private final ServerBootstrap serverBootstrap = new ServerBootstrap();

        public static ServerBuilder newBuilder() {
            return new ServerBuilder();
        }

        public ServerBuilder setEventLoopGroups(@NotNull EventLoopGroup parentGroup,
                                               @NotNull EventLoopGroup childGroup) {
            serverBootstrap.group(parentGroup, childGroup);
            return this;
        }

        public ServerBuilder setEventLoopGroup(@NotNull EventLoopGroup parentGroup) {
            serverBootstrap.group(parentGroup);
            return this;
        }

        public ServerBuilder setChannelHandler(@NotNull ChannelHandler channelHandler) {
            serverBootstrap.childHandler(channelHandler);
            return this;
        }

        public ServerBuilder setChannelHandler(@NotNull BridgenetConfiguration bridgenetConfiguration) {
            serverBootstrap.childHandler(bridgenetConfiguration);
            return this;
        }

        public <T> ServerBuilder setOption(ChannelOption<T> option, T value) {
            serverBootstrap.option(option, value);
            return this;
        }

        public <T> ServerBuilder setChildOption(ChannelOption<T> option, T value) {
            serverBootstrap.childOption(option, value);
            return this;
        }

        public BridgenetServer build() {
            return new BridgenetServer(serverBootstrap);
        }
    }
}
