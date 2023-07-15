package me.moonways.bridgenet;

import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.TestCommand;
import me.moonways.bridgenet.api.command.CommandRegistry;
import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.Inject;
import me.moonways.bridgenet.protocol.Bridgenet;
import me.moonways.bridgenet.protocol.BridgenetNetty;
import me.moonways.bridgenet.protocol.BridgenetServer;
import me.moonways.bridgenet.protocol.ProtocolControl;
import me.moonways.bridgenet.protocol.message.MessageRegistrationService;
import me.moonways.bridgenet.protocol.message.ProtocolDirection;
import me.moonways.bridgenet.protocol.pipeline.BridgenetPipeline;
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
    private CommandRegistry commandRegistry;

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
        commandRegistry.register(TestCommand.class);
    }

    private void applyDependencyInjection() {

        // local system services.
        dependencyInjection.bind(dependencyInjection);
        dependencyInjection.bind(new BridgenetConsole(this));

        // dependencies services.
        dependencyInjection.bind(ConvenoRouter.create());

        // inject
        dependencyInjection.findComponentsIntoBasePackage();
        dependencyInjection.injectFields(this);

        // bridgenet system
        dependencyInjection.bind(bridgenet);
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
