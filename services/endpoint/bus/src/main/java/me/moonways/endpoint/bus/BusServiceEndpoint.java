package me.moonways.endpoint.bus;

import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.model.bus.BusServiceModel;
import me.moonways.bridgenet.mtp.*;
import me.moonways.bridgenet.mtp.config.MTPConfiguration;
import me.moonways.bridgenet.mtp.pipeline.NettyPipelineInitializer;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;

import java.rmi.RemoteException;

public class BusServiceEndpoint extends AbstractEndpointDefinition implements BusServiceModel {

    private static final long serialVersionUID = 3915937249408474506L;

    private MTPConnectionFactory connectionFactory;

    @Inject
    private BeansService beansService;
    @Inject
    private MTPDriver driver;

    public BusServiceEndpoint() throws RemoteException {
        super();
    }

    @PostConstruct
    public void bindMTPServer() {
        connectionFactory = MTPConnectionFactory.createConnectionFactory();
        beansService.bind(connectionFactory);

        driver.bindMessages();
        driver.bindHandlers();

        bindServer();
    }

    public void bindServer() {
        ChannelFactory<? extends ServerChannel> serverChannelFactory = NettyFactory.createServerChannelFactory();

        MTPConfiguration configuration = connectionFactory.getConfiguration();
        NettyPipelineInitializer channelInitializer = NettyPipelineInitializer.create(driver, configuration);

        EventLoopGroup parentWorker = NettyFactory.createEventLoopGroup(configuration.getSettings().getWorkers().getBossThreads());
        EventLoopGroup childWorker = NettyFactory.createEventLoopGroup(configuration.getSettings().getWorkers().getChildThreads());

        MTPServer server = MTPConnectionFactory.newServerBuilder(connectionFactory)
                .setChildOption(ChannelOption.TCP_NODELAY, true)
                .setChannelFactory(serverChannelFactory)
                .setChannelInitializer(channelInitializer)
                .setGroup(parentWorker, childWorker)
                .build();

        MTPChannel channel = server.bindSync();
        beansService.bind(channel);
    }
}
