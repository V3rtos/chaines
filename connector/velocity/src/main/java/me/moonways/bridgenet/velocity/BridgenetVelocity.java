package me.moonways.bridgenet.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import me.moonways.bridgenet.connector.BaseBridgenetConnector;
import me.moonways.bridgenet.connector.cloudnet.CloudnetWrapper;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.mtp.MTPDriver;
import me.moonways.bridgenet.mtp.message.MessageRegistry;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Plugin(id = "velocity-connector", name = "VelocityConnector", version = "1.0", authors =  {"GitCoder", "xxcoldinme"})
public class BridgenetVelocity extends BaseBridgenetConnector {

    @Inject
    private MTPDriver driver;
    @Inject
    private MessageRegistry messageRegistry;
    @Inject
    private CloudnetWrapper cloudnetWrapper;
    @Inject
    private DependencyInjection injector;


    @Getter
    private final ProxyServer proxyServer;

    @Getter
    private final Logger logger;

    @com.google.inject.Inject
    public BridgenetVelocity(@NotNull ProxyServer server, @NotNull Logger logger) {
        this.proxyServer = server;
        this.logger = logger;
    }

    @PostConstruct
    private void init() {
        super.initializeConnectorData(
                driver,
                messageRegistry);
    }

    @Override
    protected void registerBridgenetMessages(@NotNull MessageRegistry registry) {
        //registry.registerAll(ProtocolDirection.TO_CLIENT);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        enableBridgenetServicesSync(injector, this);
        sendHandshakeRequest();
    }

    private void sendHandshakeRequest() {
        String fullCurrentServiceName = cloudnetWrapper.getFullCurrentServiceName();

        String currentSnapshotHost = cloudnetWrapper.getCurrentSnapshotHost();
        int currentSnapshotPort = cloudnetWrapper.getCurrentSnapshotPort();

        //VelocityHandshakeMessage velocityHandshakeMessage = new VelocityHandshakeMessage(fullCurrentServiceName, currentSnapshotHost, currentSnapshotPort);
        //sendMessage(velocityHandshakeMessage);
    }
}
