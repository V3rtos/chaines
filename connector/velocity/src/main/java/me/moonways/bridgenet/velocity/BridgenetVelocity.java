package me.moonways.bridgenet.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.dytanic.cloudnet.driver.network.HostAndPort;
import de.dytanic.cloudnet.driver.service.ServiceId;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.wrapper.Wrapper;
import lombok.Getter;
import me.moonways.bridgenet.connector.BridgenetConnector;
import me.moonways.bridgenet.protocol.*;
import me.moonways.bridgenet.protocol.message.MessageComponent;
import me.moonways.bridgenet.protocol.message.MessageHandler;
import me.moonways.bridgenet.protocol.message.MessageRegistrationService;
import me.moonways.bridgenet.protocol.message.ProtocolDirection;
import me.moonways.bridgenet.protocol.pipeline.BridgenetPipeline;
import me.moonways.bridgenet.service.inject.Component;
import me.moonways.bridgenet.service.inject.DependencyInjection;
import me.moonways.bridgenet.service.inject.Inject;
import me.moonways.bridgenet.services.connection.server.message.VelocityHandshakeMessage;
import net.conveno.jdbc.ConvenoRouter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Component
@Plugin(id = "bridgenet-velocity-connector",
        name = "VelocityConnector",
        version = "1.0",
        authors =  {
        "GitCoder", "xxcoldinme"
        })
public class BridgenetVelocity {

    @Inject
    private BridgenetConnector bridgenetConnector;

    @Inject
    private ProtocolControl protocolControl;

    @Inject
    private MessageRegistrationService messageRegistrationService;

    @Getter
    private final ProxyServer proxyServer;
    @Getter
    private final Logger logger;

    private BridgenetChannel bridgenetChannel;

    // ---------------------------------------------------------------------------------------------------------------- //

    private final DependencyInjection dependencyInjection = new DependencyInjection();

    private final Bridgenet bridgenet = Bridgenet.createByProperties();

    // ---------------------------------------------------------------------------------------------------------------- //

    @com.google.inject.Inject
    public BridgenetVelocity(@NotNull ProxyServer server, @NotNull Logger logger) {
        this.proxyServer = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        start();

        writeConnectMessage();
    }

    private void writeConnectMessage() {
        ServiceId serviceId = Wrapper.getInstance().getServiceId();
        String velocityName = serviceId.getTaskName() + "-" + serviceId.getTaskServiceId();

        ServiceInfoSnapshot serviceInfoSnapshot = Wrapper.getInstance().getCurrentServiceInfoSnapshot();
        HostAndPort serverAddress = serviceInfoSnapshot.getAddress();

        bridgenetChannel.sendMessage(new VelocityHandshakeMessage(
                velocityName, serverAddress.getHost(), serverAddress.getPort()));
    }

    private void initializeProperty() {
        // netty connection settings.
        System.setProperty(Bridgenet.DEFAULT_HOST_PROPERTY, "localhost");
        System.setProperty(Bridgenet.DEFAULT_PORT_PROPERTY, "8080");

        // jdbc settings.
        System.setProperty("system.jdbc.username", "username");
        System.setProperty("system.jdbc.password", "password");
    }

    private void start() {
        initializeProperty();
        applyDependencyInjection();
        registerMessages();

        connect();
    }

    private void connect() {
        BridgenetClient client = Bridgenet.newClientBuilder(bridgenet, protocolControl)
                .setGroup(BridgenetNetty.createEventLoopGroup(2))
                .setChannelFactory(BridgenetNetty.createClientChannelFactory())
                .setChannelInitializer(BridgenetPipeline.newBuilder(protocolControl).build())
                .build();

        this.bridgenetChannel = client.connectSync();
    }

    private void applyDependencyInjection() {

        // local system services.
        dependencyInjection.addDepend(dependencyInjection);

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

    private void registerMessages() {
        messageRegistrationService.registerAll(ProtocolDirection.TO_CLIENT);
    }
}
