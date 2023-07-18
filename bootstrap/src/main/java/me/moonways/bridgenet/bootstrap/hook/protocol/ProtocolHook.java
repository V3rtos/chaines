package me.moonways.bridgenet.bootstrap.hook.protocol;

import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.bridgenet.bootstrap.hook.console.BridgenetConsole;
import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.Inject;
import me.moonways.bridgenet.protocol.BridgenetNetty;
import me.moonways.bridgenet.protocol.BridgenetServer;
import me.moonways.bridgenet.protocol.BridgenetTCP;
import me.moonways.bridgenet.protocol.ProtocolControl;
import me.moonways.bridgenet.protocol.pipeline.BridgenetPipeline;
import org.jetbrains.annotations.NotNull;

public class ProtocolHook extends BootstrapHook {

    @Inject
    private DependencyInjection dependencyInjection;

    @Inject
    private ProtocolControl protocolControl;

    @Inject
    private BridgenetConsole bridgenetConsole;

    @Override
    public void setProperties() {
        System.setProperty(BridgenetTCP.DEFAULT_HOST_PROPERTY, "localhost");
        System.setProperty(BridgenetTCP.DEFAULT_PORT_PROPERTY, "8080");
    }

    @Override
    protected void postExecute(@NotNull AppBootstrap bootstrap) {
        BridgenetTCP bridgenetTCP = BridgenetTCP.createByProperties();
        dependencyInjection.bind(bridgenetTCP);

        startConnection(bridgenetTCP);
    }

    public void startConnection(BridgenetTCP bridgenetTCP) {
        ChannelFactory<? extends ServerChannel> serverChannelFactory = BridgenetNetty.createServerChannelFactory();
        BridgenetPipeline channelInitializer = BridgenetPipeline.newBuilder(protocolControl).build();

        EventLoopGroup parentWorker = BridgenetNetty.createEventLoopGroup(2);
        EventLoopGroup childWorker = BridgenetNetty.createEventLoopGroup(4);

        BridgenetServer server = BridgenetTCP.newServerBuilder(bridgenetTCP, protocolControl)
                .setGroup(parentWorker, childWorker)
                .setChannelFactory(serverChannelFactory)
                .setChannelInitializer(channelInitializer)
                .build();

        server.bind().thenAccept(channel -> dependencyInjection.bind(channel));
        // TODO - 17.07.2023 - Перенести в bus endpoint
    }
}
