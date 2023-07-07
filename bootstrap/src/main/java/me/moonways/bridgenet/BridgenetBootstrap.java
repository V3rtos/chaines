package me.moonways.bridgenet;

import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.BridgenetControl;
import me.moonways.bridgenet.api.TestCommand;
import me.moonways.bridgenet.protocol.*;
import me.moonways.bridgenet.protocol.message.*;
import me.moonways.bridgenet.protocol.pipeline.BridgenetPipeline;
import me.moonways.bridgenet.service.inject.DependencyInjection;
import me.moonways.bridgenet.service.inject.Inject;
import net.conveno.jdbc.ConvenoRouter;

@Log4j2
public class BridgenetBootstrap {

    @Inject
    private ProtocolControl protocolControl;

    @Inject
    private BridgenetConsole bridgenetConsole;

    @Inject
    private MessageRegistrationService messageRegistrationService;

    @Inject
    @Getter
    private BridgenetControl bridgenetControl;

// ---------------------------------------------------------------------------------------------------------------- //

    private final DependencyInjection dependencyInjection = new DependencyInjection();

    private final Bridgenet bridgenet = Bridgenet.createByProperties();

// ---------------------------------------------------------------------------------------------------------------- //

    public void start() {
        applyDependencyInjection();
        registerInternalCommands();
        registerMessages();

        startConnection();

        bridgenetConsole.start();
    }

    public void startConnection() {
        ChannelFactory<? extends ServerChannel> serverChannelFactory = BridgenetNetty.createServerChannelFactory();
        BridgenetPipeline channelInitializer = BridgenetPipeline.newBuilder(protocolControl).build();

        EventLoopGroup parentWorker = BridgenetNetty.createEventLoopGroup(2);
        EventLoopGroup childWorker = BridgenetNetty.createEventLoopGroup(4);

        BridgenetServer server = Bridgenet.newServerBuilder(bridgenet, protocolControl)
                .setGroup(parentWorker, childWorker)
                .setChannelFactory(serverChannelFactory)
                .setChannelInitializer(channelInitializer)
                .build();

        server.bindSync();
    }

    private void registerMessages() {
        messageRegistrationService.registerAll(ProtocolDirection.TO_SERVER);
    }

    private void registerInternalCommands() {
        bridgenetControl.registerCommand(TestCommand.class);
    }

    private void applyDependencyInjection() {

        // local system services.
        dependencyInjection.addDepend(dependencyInjection);
        dependencyInjection.addDepend(new BridgenetConsole(this));

        // dependencies services.
        dependencyInjection.addDepend(ConvenoRouter.create());

        // inject
        dependencyInjection.scanDependenciesOfBasicPackage();
        dependencyInjection.injectDependencies(this);

        dependencyInjection.scanDependenciesOfBasicPackage(MessageComponent.class);
        dependencyInjection.scanDependenciesOfBasicPackage(MessageHandler.class);

        // bridgenet system
        dependencyInjection.addDepend(bridgenet);
    }

    public static void main(String[] args) {
        // netty connection settings.
        System.setProperty(Bridgenet.DEFAULT_HOST_PROPERTY, "localhost");
        System.setProperty(Bridgenet.DEFAULT_PORT_PROPERTY, "8080");

        // jdbc settings.
        System.setProperty("system.jdbc.username", "username");
        System.setProperty("system.jdbc.password", "password");

        // run bridgenet server system.
        BridgenetBootstrap bootstrap = new BridgenetBootstrap();
        bootstrap.start();
    }
}
