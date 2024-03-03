package me.moonways.bridgenet.mtp.connection.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import me.moonways.bridgenet.mtp.channel.ChannelDirection;
import me.moonways.bridgenet.mtp.channel.NetworkChannelFactory;
import me.moonways.bridgenet.mtp.connection.AbstractNetworkConnection;

import java.util.concurrent.CompletableFuture;

public class BridgenetNetworkServer extends AbstractNetworkConnection {

    @Inject
    private NetworkChannelFactory networkChannelFactory;
    @Inject
    private BeansService beansService;

    public BridgenetNetworkServer(ServerBootstrap serverBootstrap) {
        super(serverBootstrap, null);
    }

    @Override
    protected BridgenetNetworkChannel prepareChannelSuccess(Channel channel) {
        BridgenetNetworkChannel bridgenetNetworkChannel = networkChannelFactory.create(ChannelDirection.TO_CLIENT, channel);
        beansService.bind(bridgenetNetworkChannel);

        return bridgenetNetworkChannel;
    }

    @Override
    public BridgenetNetworkChannel connectSync() {
        throw new UnsupportedOperationException("connection is server");
    }

    @Override
    public CompletableFuture<BridgenetNetworkChannel> connect() {
        throw new UnsupportedOperationException("connection is server");
    }
}
