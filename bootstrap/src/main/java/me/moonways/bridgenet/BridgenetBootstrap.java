package me.moonways.bridgenet;

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
    @Getter
    private BridgenetControl bridgenetControl;

    @Inject
    private BridgenetConsole bridgenetConsole;

    @Inject
    private MessageRegistrationService messageRegistrationService;

    private final DependencyInjection dependencyInjection = new DependencyInjection();

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

    public void start() {
        applyDependencyInjection();
        registerInternalCommands();
        registerMessages();

        startConnection();

        bridgenetConsole.start();
    }

    private final Bridgenet bridgenet = Bridgenet.createByProperties();

    @Inject
    private ProtocolControl protocolControl;

    public void startConnection() {
        BridgenetServer server = Bridgenet.newServerBuilder(bridgenet, protocolControl)
                .setGroup(BridgenetNetty.createEventLoopGroup(2), BridgenetNetty.createEventLoopGroup(4))
                .setChannelFactory(BridgenetNetty.createServerChannelFactory())
                .setChannelInitializer(BridgenetPipeline.newBuilder(protocolControl).build())
                .build();

        server.bindSync();
    }

    private void registerMessages() {
        messageRegistrationService.registerAll(ProtocolDirection.TO_SERVER);
    }

    private void registerInternalCommands() {
        bridgenetControl.registerCommand(TestCommand.class);
    }

    public static void main(String[] args) {
        System.setProperty(Bridgenet.DEFAULT_HOST_PROPERTY, "localhost");
        System.setProperty(Bridgenet.DEFAULT_PORT_PROPERTY, "8080");

        BridgenetBootstrap bootstrap = new BridgenetBootstrap();
        bootstrap.start();
    }
}
