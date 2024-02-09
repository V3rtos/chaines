package me.moonways.bridgenet.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansScanningService;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.inject.bean.service.BeansStore;
import me.moonways.bridgenet.connector.BridgenetConnector;
import me.moonways.bridgenet.connector.BridgenetServerSync;
import me.moonways.bridgenet.connector.cloudnet.CloudnetWrapper;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.mtp.MTPMessageSender;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.UUID;

@Plugin(id = "velocity-connector", name = "BridgeNetSync", version = "1.0", authors =  {"GitCoder", "lyx"})
public class BridgenetVelocityConnector extends BridgenetConnector {

    @Inject
    private CloudnetWrapper cloudnetWrapper;
    @Inject
    private BeansService beansService; // todo - заинжектить плагины, которые будут инициализироваться после коннектора в Velocity.
    @Inject
    private BeansStore beansStore;
    @Inject
    private BeansScanningService beansScanner;

    @Getter
    private final ProxyServer proxyServer;
    @Getter
    private final Logger logger;

    @com.google.inject.Inject
    public BridgenetVelocityConnector(@NotNull ProxyServer server, @NotNull Logger logger) {
        this.proxyServer = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        super.doConnectBasically();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        BridgenetServerSync bridgenet = getBridgenetServerSync();
        bridgenet.sendServerDisconnect();
    }

    @Override
    public void onConnected(MTPMessageSender channel) {
        beansService.bind(new CloudnetWrapper());

        BridgenetServerSync bridgenet = getBridgenetServerSync();

        Handshake.Result result = bridgenet.sendServerHandshake(
                cloudnetWrapper.getFullCurrentServiceName(),
                cloudnetWrapper.getCurrentSnapshotHost(),
                cloudnetWrapper.getCurrentSnapshotPort());

        handleHandshakeResult(result);
    }

    private void handleHandshakeResult(Handshake.Result result) {
        UUID serverUuid = result.getKey();

        if (result instanceof Handshake.Failure) {

            logger.info("§4Handshake failed: Server has already registered by " + serverUuid);
            proxyServer.shutdown();
        }
    }
}
