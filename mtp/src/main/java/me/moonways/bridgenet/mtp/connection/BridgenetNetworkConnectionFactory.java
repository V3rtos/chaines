package me.moonways.bridgenet.mtp.connection;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.BeanFactory;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.factory.FactoryType;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.assembly.ResourcesAssembly;
import me.moonways.bridgenet.assembly.ResourcesTypes;
import me.moonways.bridgenet.mtp.config.NetworkJsonConfiguration;
import me.moonways.bridgenet.mtp.config.descriptor.NetworkSettingsDescriptor;
import me.moonways.bridgenet.mtp.connection.client.BridgenetNetworkClient;
import me.moonways.bridgenet.mtp.connection.client.BridgenetNetworkClientHandler;
import me.moonways.bridgenet.mtp.connection.client.NetworkClientReconnectionHandler;
import me.moonways.bridgenet.mtp.connection.server.BridgenetNetworkServer;
import me.moonways.bridgenet.mtp.inbound.InboundChannelOptionsHandler;
import me.moonways.bridgenet.mtp.message.encryption.MessageEncryption;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Getter
@RequiredArgsConstructor
@Autobind(provider = FactoryType.METHOD)
public class BridgenetNetworkConnectionFactory {

    @BeanFactory
    private static BridgenetNetworkConnectionFactory newInstance(BeansService beansService, ResourcesAssembly assembly) {
        NetworkSettingsDescriptor settings = assembly.readJsonAtEntity(ResourcesTypes.MTP_CONFIG_JSON, NetworkSettingsDescriptor.class);
        NetworkJsonConfiguration configuration = new NetworkJsonConfiguration(settings, new MessageEncryption(settings.getSecurity()));

        beansService.bind(configuration);

        InetSocketAddress socketAddress =
                new InetSocketAddress(
                        configuration.getSettings().getHost(),
                        configuration.getSettings().getPort()
                );
        return new BridgenetNetworkConnectionFactory(configuration, socketAddress);
    }

    private final NetworkJsonConfiguration configuration;
    private final SocketAddress socketAddress;

    @Inject
    private BeansService beansService;

    public NetworkClientReconnectionHandler newClientReconnectionHandler(BridgenetNetworkConnection client, BridgenetNetworkClientHandler networkClientHandler) {
        NetworkClientReconnectionHandler reconnectionHandler = new NetworkClientReconnectionHandler(client, networkClientHandler);
        beansService.inject(reconnectionHandler);
        return reconnectionHandler;
    }

    public ServerBuilder newServerBuilder() {
        return new ServerBuilder();
    }

    public ClientBuilder newClientBuilder() {
        return new ClientBuilder();
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class ServerBuilder {
        private final ServerBootstrap serverBootstrap = new ServerBootstrap();

        public ServerBuilder setGroup(@NotNull EventLoopGroup parentGroup,
                                      @NotNull EventLoopGroup childGroup) {
            serverBootstrap.group(parentGroup, childGroup);
            return this;
        }

        public ServerBuilder setChannelFactory(@NotNull ChannelFactory<? extends ServerChannel> channelFactory) {
            serverBootstrap.channelFactory(channelFactory);
            return this;
        }

        public ServerBuilder setChannelInitializer(@NotNull InboundChannelOptionsHandler optionsHandler) {
            serverBootstrap.childHandler(optionsHandler);
            return this;
        }

        public BridgenetNetworkConnection build() {
            serverBootstrap.localAddress(socketAddress);

            BridgenetNetworkServer bridgenetNetworkServer = new BridgenetNetworkServer(serverBootstrap);
            beansService.inject(bridgenetNetworkServer);

            return bridgenetNetworkServer;
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class ClientBuilder {
        private final Bootstrap bootstrap = new Bootstrap();

        public ClientBuilder setGroup(@NotNull EventLoopGroup parentGroup) {
            bootstrap.group(parentGroup);
            return this;
        }

        public ClientBuilder setChannelInitializer(@NotNull InboundChannelOptionsHandler nettyPipeline) {
            bootstrap.handler(nettyPipeline);
            return this;
        }

        public ClientBuilder setChannelFactory(@NotNull ChannelFactory<? extends Channel> channelFactory) {
            bootstrap.channelFactory(channelFactory);
            return this;
        }

        public BridgenetNetworkConnection build() {
            bootstrap.remoteAddress(socketAddress);

            BridgenetNetworkClient bridgenetNetworkClient = new BridgenetNetworkClient(bootstrap);
            beansService.inject(bridgenetNetworkClient);

            return bridgenetNetworkClient;
        }
    }
}
