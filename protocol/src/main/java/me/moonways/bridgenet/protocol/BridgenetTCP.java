package me.moonways.bridgenet.protocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.protocol.pipeline.BridgenetPipeline;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Getter
@RequiredArgsConstructor
public class BridgenetTCP {

    public static final String DEFAULT_HOST_PROPERTY = "bridgenet.address.host";
    public static final String DEFAULT_PORT_PROPERTY = "bridgenet.address.port";

    public static BridgenetTCP createByProperties(@NotNull String hostProperty, @NotNull String portProperty) {
        String host = System.getProperty(hostProperty);
        Integer port = Integer.getInteger(portProperty);

        SocketAddress socketAddress = new InetSocketAddress(host, port);
        return new BridgenetTCP(socketAddress);
    }

    public static BridgenetTCP createByProperties() {
        return createByProperties(DEFAULT_HOST_PROPERTY, DEFAULT_PORT_PROPERTY);
    }

    public static ServerBuilder newServerBuilder(@NotNull BridgenetTCP bridgenetTCP, @NotNull ProtocolControl protocolControl) {
        return new ServerBuilder(bridgenetTCP, protocolControl);
    }

    public static ClientBuilder newClientBuilder(@NotNull BridgenetTCP bridgenetTCP, @NotNull ProtocolControl protocolControl) {
        return new ClientBuilder(bridgenetTCP, protocolControl);
    }

    @Getter
    private final SocketAddress socketAddress;

    @RequiredArgsConstructor
    public static class ServerBuilder {

        private final BridgenetTCP bridgenetTCP;
        private final ProtocolControl protocolControl;

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

        public ServerBuilder setChannelInitializer(@NotNull BridgenetPipeline bridgenetPipeline) {
            serverBootstrap.childHandler(bridgenetPipeline);
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
            serverBootstrap.localAddress(bridgenetTCP.getSocketAddress());
            return new BridgenetServer(serverBootstrap, protocolControl);
        }
    }

    @RequiredArgsConstructor
    public static class ClientBuilder {

        private final BridgenetTCP bridgenetTCP;
        private final ProtocolControl protocolControl;

        private final Bootstrap bootstrap = new Bootstrap();

        public ClientBuilder setGroup(@NotNull EventLoopGroup parentGroup) {
            bootstrap.group(parentGroup);
            return this;
        }

        public ClientBuilder setChannelInitializer(@NotNull BridgenetPipeline bridgenetPipeline) {
            bootstrap.handler(bridgenetPipeline);
            return this;
        }

        public ClientBuilder setChannelFactory(@NotNull ChannelFactory<? extends Channel> channelFactory) {
            bootstrap.channelFactory(channelFactory);
            return this;
        }

        public <T> ClientBuilder setOption(@NotNull ChannelOption<T> option, @Nullable T value) {
            bootstrap.option(option, value);
            return this;
        }

        public BridgenetClient build() {
            bootstrap.remoteAddress(bridgenetTCP.getSocketAddress());
            return new BridgenetClient(bootstrap, protocolControl);
        }
    }
}
