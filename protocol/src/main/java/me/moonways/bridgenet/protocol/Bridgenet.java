package me.moonways.bridgenet.protocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.protocol.message.MessageContainer;
import me.moonways.bridgenet.protocol.pipeline.BridgenetSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@RequiredArgsConstructor
@Getter
public class Bridgenet {

    public static final String DEFAULT_HOST_PROPERTY = "bridgenet.address.host";
    public static final String DEFAULT_PORT_PROPERTY = "bridgenet.address.port";

    public static Bridgenet createByProperties(@NotNull String hostProperty, @NotNull String portProperty) {
        String host = System.getProperty(hostProperty);
        Integer port = Integer.getInteger(portProperty);

        SocketAddress socketAddress = new InetSocketAddress(host, port);
        return new Bridgenet(socketAddress);
    }

    public static Bridgenet createByProperties() {
        return createByProperties(DEFAULT_HOST_PROPERTY, DEFAULT_PORT_PROPERTY);
    }

    public static ServerBuilder newServerBuilder(@NotNull Bridgenet bridgenet) {
        return new ServerBuilder(bridgenet);
    }

    public static ClientBuilder newClientBuilder(@NotNull Bridgenet bridgenet) {
        return new ClientBuilder(bridgenet);
    }

    private final MessageContainer messageContainer = MessageContainer.createContainers();

    @Getter
    private final SocketAddress socketAddress;

    @RequiredArgsConstructor
    public static class ServerBuilder {

        private final Bridgenet bridgenet;

        private final ServerBootstrap serverBootstrap = new ServerBootstrap();

        public ServerBuilder setGroup(@NotNull EventLoopGroup parentGroup,
                                      @NotNull EventLoopGroup childGroup) {
            serverBootstrap.group(parentGroup, childGroup);
            return this;
        }

        public ServerBuilder setGroup(@NotNull EventLoopGroup parentGroup) {
            serverBootstrap.group(parentGroup);
            return this;
        }

        public ServerBuilder setChannelFactory(@NotNull ChannelFactory<? extends ServerChannel> channelFactory) {
            serverBootstrap.channelFactory(channelFactory);
            return this;
        }

        public ServerBuilder setChannelHandler(@NotNull ChannelHandler channelHandler) {
            serverBootstrap.childHandler(channelHandler);
            return this;
        }

        public ServerBuilder setSettings(@NotNull BridgenetSettings bridgenetSettings) {
            serverBootstrap.childHandler(bridgenetSettings);
            return this;
        }

        public <T> ServerBuilder setOption(@NotNull ChannelOption<T> option, @Nullable T value) {
            serverBootstrap.option(option, value);
            return this;
        }

        public <T> ServerBuilder setChildOption(@NotNull ChannelOption<T> option, @Nullable T value) {
            serverBootstrap.childOption(option, value);
            return this;
        }

        public BridgenetServer build() {
            serverBootstrap.localAddress(bridgenet.getSocketAddress());
            return new BridgenetServer(serverBootstrap, bridgenet);
        }
    }

    @RequiredArgsConstructor
    public static class ClientBuilder {

        private final Bridgenet bridgenet;

        private final Bootstrap bootstrap = new Bootstrap();

        public ClientBuilder setGroup(@NotNull EventLoopGroup parentGroup) {
            bootstrap.group(parentGroup);
            return this;
        }

        public ClientBuilder setChannelHandler(@NotNull ChannelHandler channelHandler) {
            bootstrap.handler(channelHandler);
            return this;
        }

        public ClientBuilder setSettings(@NotNull BridgenetSettings bridgenetSettings) {
            bootstrap.handler(bridgenetSettings);
            return this;
        }

        public <T> ClientBuilder setOption(@NotNull ChannelOption<T> option, @Nullable T value) {
            bootstrap.option(option, value);
            return this;
        }

        public BridgenetClient build() {
            bootstrap.remoteAddress(bridgenet.getSocketAddress());
            return new BridgenetClient(bootstrap, bridgenet);
        }
    }
}
