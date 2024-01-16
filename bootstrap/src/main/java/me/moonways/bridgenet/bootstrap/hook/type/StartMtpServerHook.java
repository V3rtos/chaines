package me.moonways.bridgenet.bootstrap.hook.type;

import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.ApplicationBootstrapHook;
import me.moonways.bridgenet.bootstrap.hook.type.console.BridgenetConsole;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.*;
import me.moonways.bridgenet.mtp.config.MTPConfiguration;
import me.moonways.bridgenet.mtp.pipeline.NettyPipeline;
import org.jetbrains.annotations.NotNull;

public class StartMtpServerHook extends ApplicationBootstrapHook {

    @Inject
    private DependencyInjection injector;

    @Inject
    private MTPDriver driver;

    @Inject
    private BridgenetConsole bridgenetConsole;

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) {
        MTPConnectionFactory connectionFactory = MTPConnectionFactory.createConnectionFactory(injector);
        injector.bind(connectionFactory);

        driver.bindMessages();
        driver.bindHandlers();

        startConnection(connectionFactory);
    }

    public void startConnection(MTPConnectionFactory connectionFactory) {
        ChannelFactory<? extends ServerChannel> serverChannelFactory = NettyFactory.createServerChannelFactory();

        MTPConfiguration configuration = connectionFactory.getConfiguration();
        NettyPipeline channelInitializer = NettyPipeline.create(driver, configuration);

        EventLoopGroup parentWorker = NettyFactory.createEventLoopGroup(configuration.getSettings().getWorkers().getBossThreads());
        EventLoopGroup childWorker = NettyFactory.createEventLoopGroup(configuration.getSettings().getWorkers().getChildThreads());

        MTPServer server = MTPConnectionFactory.newServerBuilder(connectionFactory)
                .setChildOption(ChannelOption.TCP_NODELAY, true)
                .setChannelFactory(serverChannelFactory)
                .setChannelInitializer(channelInitializer)
                .setGroup(parentWorker, childWorker)
                .build();

        MTPChannel channel = server.bindSync();
        injector.bind(channel);
        // TODO - 17.07.2023 - Перенести в bus endpoint
    }
}
