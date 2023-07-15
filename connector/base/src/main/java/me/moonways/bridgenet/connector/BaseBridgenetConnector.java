package me.moonways.bridgenet.connector;

import lombok.Getter;
import lombok.Setter;
import me.moonways.bridgenet.connector.reconnect.BridgenetReconnectHandler;
import me.moonways.bridgenet.protocol.*;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageComponent;
import me.moonways.bridgenet.protocol.message.MessageHandler;
import me.moonways.bridgenet.protocol.message.MessageRegistrationService;
import me.moonways.bridgenet.protocol.pipeline.BridgenetPipeline;
import me.moonways.bridgenet.injection.DependencyInjection;
import net.conveno.jdbc.ConvenoRouter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class BaseBridgenetConnector {

    private Bridgenet bridgenet;

    private ProtocolControl protocolControl;
    private MessageRegistrationService messageRegistrationService;

    @Setter
    @Getter
    private BridgenetChannel channel;

    protected void initializeConnectorData(ProtocolControl protocolControl,
                                           MessageRegistrationService messageRegistrationService) {

        this.protocolControl = protocolControl;
        this.messageRegistrationService = messageRegistrationService;
    }

    protected void registerBridgenetMessages(@NotNull MessageRegistrationService messageRegistrationService) {
        // override me.
    }

    public void setProperties() {
        // netty connection settings.
        System.setProperty(Bridgenet.DEFAULT_HOST_PROPERTY, "localhost");
        System.setProperty(Bridgenet.DEFAULT_PORT_PROPERTY, "8080");

        // jdbc settings.
        System.setProperty("system.jdbc.username", "username");
        System.setProperty("system.jdbc.password", "password");
    }

    private void applyDependencyInjection(BaseBridgenetConnector currentConnector) {
        DependencyInjection dependencyInjection = new DependencyInjection();

        // local system services.
        dependencyInjection.bind(dependencyInjection);

        // dependencies services.
        dependencyInjection.bind(ConvenoRouter.create());

        // inject
        dependencyInjection.findComponentsIntoBasePackage();
        dependencyInjection.bind(currentConnector);

        dependencyInjection.findComponentsIntoBasePackage(MessageComponent.class);
        dependencyInjection.findComponentsIntoBasePackage(MessageHandler.class);

        // bridgenet system
        dependencyInjection.bind(bridgenet);
    }

    public void enableBridgenetServicesSync(BaseBridgenetConnector currentConnector) {
        setProperties();
        bridgenet = Bridgenet.createByProperties();

        applyDependencyInjection(currentConnector);

        registerBridgenetMessages(messageRegistrationService);
        syncBridgenetConnection(bridgenet);
    }

    public void syncBridgenetConnection(@NotNull Bridgenet bridgenet) {
        BridgenetPipeline bridgenetPipeline = BridgenetPipeline.
                newBuilder(protocolControl).build();

        BridgenetClient client = Bridgenet.newClientBuilder(bridgenet, protocolControl)
                .setGroup(BridgenetNetty.createEventLoopGroup(2))
                .setChannelFactory(BridgenetNetty.createClientChannelFactory())
                .setChannelInitializer(bridgenetPipeline)
                .build();

        bridgenetPipeline.addChannelHandler(new BridgenetReconnectHandler(client, this));
        channel = client.connectSync();
    }

    public void sendMessage(Message message) {
        channel.sendMessage(message);
    }

    public <M extends Message> CompletableFuture<M> sendMessageWithCallback(Message message) {
        return channel.sendMessageWithCallback(message);
    }
}
