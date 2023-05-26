package me.moonways.bridgenet.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import me.moonways.bridgenet.connector.BaseBridgenetConnector;
import me.moonways.bridgenet.connector.cloudnet.CloudnetWrapper;
import me.moonways.bridgenet.protocol.ProtocolControl;
import me.moonways.bridgenet.protocol.message.MessageRegistrationService;
import me.moonways.bridgenet.protocol.message.ProtocolDirection;
import me.moonways.bridgenet.service.inject.InitMethod;
import me.moonways.bridgenet.service.inject.Inject;
import me.moonways.bridgenet.services.connection.server.protocol.handshake.VelocityHandshakeMessage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Plugin(id = "velocity-connector", name = "VelocityConnector", version = "1.0", authors =  {"GitCoder", "xxcoldinme"})
public class BridgenetVelocity extends BaseBridgenetConnector {

    @Inject
    private ProtocolControl protocolControl;
    @Inject
    private MessageRegistrationService messageRegistrationService;
    @Inject
    private CloudnetWrapper cloudnetWrapper;


    @Getter
    private final ProxyServer proxyServer;

    @Getter
    private final Logger logger;

    @com.google.inject.Inject
    public BridgenetVelocity(@NotNull ProxyServer server, @NotNull Logger logger) {
        this.proxyServer = server;
        this.logger = logger;
    }

    @InitMethod
    private void init() {
        super.initializeConnectorData(
                protocolControl,
                messageRegistrationService);
    }

    @Override
    protected void registerBridgenetMessages(@NotNull MessageRegistrationService registry) {
        registry.registerAll(ProtocolDirection.TO_CLIENT);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        enableBridgenetServicesSync(this);
        sendHandshakeRequest();
    }

    private void sendHandshakeRequest() {
        String fullCurrentServiceName = cloudnetWrapper.getFullCurrentServiceName();

        String currentSnapshotHost = cloudnetWrapper.getCurrentSnapshotHost();
        int currentSnapshotPort = cloudnetWrapper.getCurrentSnapshotPort();

        VelocityHandshakeMessage velocityHandshakeMessage = new VelocityHandshakeMessage(fullCurrentServiceName, currentSnapshotHost, currentSnapshotPort);
        sendMessage(velocityHandshakeMessage);
    }
}
