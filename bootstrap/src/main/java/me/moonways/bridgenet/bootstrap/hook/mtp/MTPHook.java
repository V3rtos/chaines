package me.moonways.bridgenet.bootstrap.hook.mtp;

import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.bridgenet.bootstrap.hook.console.BridgenetConsole;
import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.Inject;
import me.moonways.bridgenet.mtp.*;
import me.moonways.bridgenet.mtp.config.MTPConfiguration;
import me.moonways.bridgenet.mtp.message.DefaultMessage;
import me.moonways.bridgenet.mtp.pipeline.NettyPipeline;
import org.jetbrains.annotations.NotNull;

public class MTPHook extends BootstrapHook {

    @Inject
    private DependencyInjection dependencyInjection;

    @Inject
    private MTPDriver driver;

    @Inject
    private BridgenetConsole bridgenetConsole;

    @Override
    protected void postExecute(@NotNull AppBootstrap bootstrap) {
        MTPConnectionFactory connectionFactory = MTPConnectionFactory.createConnectionFactory(dependencyInjection);
        dependencyInjection.bind(connectionFactory);

        driver.bindMessages();

        startConnection(connectionFactory);
    }

    public void startConnection(MTPConnectionFactory connectionFactory) {
        ChannelFactory<? extends ServerChannel> serverChannelFactory = NettyFactory.createServerChannelFactory();

        MTPConfiguration configuration = connectionFactory.getConfiguration();
        NettyPipeline channelInitializer = NettyPipeline.create(driver, configuration);

        EventLoopGroup parentWorker = NettyFactory.createEventLoopGroup(configuration.getCredentials().getWorkers().getBossThreads());
        EventLoopGroup childWorker = NettyFactory.createEventLoopGroup(configuration.getCredentials().getWorkers().getChildThreads());

        MTPServer server = MTPConnectionFactory.newServerBuilder(connectionFactory)
                .setChildOption(ChannelOption.TCP_NODELAY, true)
                .setChannelFactory(serverChannelFactory)
                .setChannelInitializer(channelInitializer)
                .setGroup(parentWorker, childWorker)
                .build();

        MTPChannel channel = server.bindSync();
        dependencyInjection.bind(channel);
        // TODO - 17.07.2023 - Перенести в bus endpoint
    }
}
