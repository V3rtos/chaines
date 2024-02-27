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
import me.moonways.bridgenet.connector.description.DeviceDescription;
import me.moonways.bridgenet.connector.cloudnet.CloudnetWrapper;
import me.moonways.bridgenet.model.bus.message.Handshake;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

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
        start();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        BridgenetServerSync bridgenet = getBridgenetServerSync();
        bridgenet.exportDeviceDisconnect();
    }

    @Override
    protected DeviceDescription createDescription() {
        CloudnetWrapper cloudnetWrapper = new CloudnetWrapper();
        beansService.bind(cloudnetWrapper);

        return DeviceDescription.builder()
                .name(cloudnetWrapper.getFullCurrentServiceName())
                .host(cloudnetWrapper.getCurrentSnapshotHost())
                .port(cloudnetWrapper.getCurrentSnapshotPort())
                .build();
    }

    @Override
    public void onHandshake(Handshake.Result result) {
        result.onSuccess(() -> beansService.bind(this));
        result.onFailure(() -> {

            logger.info("§4Handshake failed: Server has already registered by " + result.getKey());
            proxyServer.shutdown();
        });
    }
}
