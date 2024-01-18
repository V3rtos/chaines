package me.moonways.bridgenet.test.engine.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import lombok.Getter;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.*;
import me.moonways.bridgenet.mtp.pipeline.NettyPipelineInitializer;

@Autobind
public class TestMTPClientConnection {

    @Inject
    private DependencyInjection injector;
    @Inject
    private MTPDriver driver;

    private MTPConnectionFactory connectionFactory;

    @Getter
    private MTPChannel channel;

    private void connect() {
        ChannelFactory<? extends Channel> clientChannelFactory = NettyFactory.createClientChannelFactory();

        NettyPipelineInitializer channelInitializer = NettyPipelineInitializer.create(driver, connectionFactory.getConfiguration());
        EventLoopGroup parentWorker = NettyFactory.createEventLoopGroup(2);

        MTPClient client = MTPConnectionFactory.newClientBuilder(connectionFactory)
                .setGroup(parentWorker)
                .setChannelFactory(clientChannelFactory)
                .setChannelInitializer(channelInitializer)
                .build();

        channel = client.connectSync();
        channel.initAttributes();

        injector.injectFields(channel);
    }

    public void prepareTest() {
        connectionFactory = MTPConnectionFactory.createConnectionFactory(injector);
        connect();
    }
}
