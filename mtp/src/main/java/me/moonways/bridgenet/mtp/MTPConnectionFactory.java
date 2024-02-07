package me.moonways.bridgenet.mtp;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.mtp.config.MTPConfiguration;
import me.moonways.bridgenet.mtp.config.Settings;
import me.moonways.bridgenet.mtp.pipeline.NettyPipelineInitializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Getter
@RequiredArgsConstructor
public class MTPConnectionFactory {

    public static MTPConnectionFactory createConnectionFactory() {
        MTPConfiguration configuration = new MTPConfiguration();
        configuration.reload();

        Settings settings = configuration.getSettings();

        return new MTPConnectionFactory(configuration,
                new InetSocketAddress(settings.getHost(), settings.getPort())
        );
    }

    public static ServerBuilder newServerBuilder(@NotNull MTPConnectionFactory factory) {
        return new ServerBuilder(factory);
    }

    public static ClientBuilder newClientBuilder(@NotNull MTPConnectionFactory factory) {
        return new ClientBuilder(factory);
    }

    private final MTPConfiguration configuration;
    private final SocketAddress socketAddress;

    @RequiredArgsConstructor
    public static class ServerBuilder {

        private final MTPConnectionFactory connectionProperties;
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

        public ServerBuilder setChannelInitializer(@NotNull NettyPipelineInitializer nettyPipeline) {
            serverBootstrap.childHandler(nettyPipeline);
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

        public MTPServer build() {
            serverBootstrap.localAddress(connectionProperties.getSocketAddress());
            return new MTPServer(serverBootstrap);
        }
    }

    @RequiredArgsConstructor
    public static class ClientBuilder {

        private final MTPConnectionFactory connectionProperties;

        private final Bootstrap bootstrap = new Bootstrap();

        public ClientBuilder setGroup(@NotNull EventLoopGroup parentGroup) {
            bootstrap.group(parentGroup);
            return this;
        }

        public ClientBuilder setChannelInitializer(@NotNull NettyPipelineInitializer nettyPipeline) {
            bootstrap.handler(nettyPipeline);
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

        public MTPClient build() {
            bootstrap.remoteAddress(connectionProperties.getSocketAddress());
            return new MTPClient(bootstrap);
        }
    }
}
