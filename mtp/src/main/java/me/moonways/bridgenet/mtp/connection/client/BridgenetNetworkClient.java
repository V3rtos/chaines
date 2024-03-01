package me.moonways.bridgenet.mtp.connection.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import me.moonways.bridgenet.mtp.channel.ChannelDirection;
import me.moonways.bridgenet.mtp.channel.NetworkChannelFactory;
import me.moonways.bridgenet.mtp.connection.AbstractNetworkConnection;

import java.util.concurrent.CompletableFuture;

public class BridgenetNetworkClient extends AbstractNetworkConnection {

    @Inject
    private BeansService beansService;
    @Inject
    private NetworkChannelFactory networkChannelFactory;

    public BridgenetNetworkClient(Bootstrap clientBootstrap) {
        super(null, clientBootstrap);
    }

    @Override
    protected BridgenetNetworkChannel prepareChannelSuccess(Channel channel) {
        BridgenetNetworkChannel bridgenetNetworkChannel = networkChannelFactory.create(ChannelDirection.TO_SERVER, channel);

        beansService.fakeBind(bridgenetNetworkChannel);
        return bridgenetNetworkChannel;
    }

    @Override
    public BridgenetNetworkChannel bindSync() {
        throw new UnsupportedOperationException("connection is client");
    }

    @Override
    public CompletableFuture<BridgenetNetworkChannel> bind() {
        throw new UnsupportedOperationException("connection is client");
    }
}
