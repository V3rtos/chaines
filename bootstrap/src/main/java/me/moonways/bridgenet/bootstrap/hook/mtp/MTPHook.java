package me.moonways.bridgenet.bootstrap.hook.mtp;

import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.bridgenet.bootstrap.hook.console.BridgenetConsole;
import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.Inject;
import me.moonways.bridgenet.mtp.NettyFactory;
import me.moonways.bridgenet.mtp.MTPServer;
import me.moonways.bridgenet.mtp.MTPConnectionFactory;
import me.moonways.bridgenet.mtp.MTPDriver;
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
        MTPConnectionFactory connectionProperties = MTPConnectionFactory.createConnectionFactory(dependencyInjection);
        dependencyInjection.bind(connectionProperties);

        startConnection(connectionProperties);
    }

    public void startConnection(MTPConnectionFactory connectionProperties) {
        ChannelFactory<? extends ServerChannel> serverChannelFactory = NettyFactory.createServerChannelFactory();
        NettyPipeline channelInitializer = NettyPipeline.create(driver);

        EventLoopGroup parentWorker = NettyFactory.createEventLoopGroup(2);
        EventLoopGroup childWorker = NettyFactory.createEventLoopGroup(4);

        MTPServer server = MTPConnectionFactory.newServerBuilder(connectionProperties)
                .setGroup(parentWorker, childWorker)
                .setChannelFactory(serverChannelFactory)
                .setChannelInitializer(channelInitializer)
                .build();

        dependencyInjection.bind(server.bindSync());
        // TODO - 17.07.2023 - Перенести в bus endpoint
    }
}
