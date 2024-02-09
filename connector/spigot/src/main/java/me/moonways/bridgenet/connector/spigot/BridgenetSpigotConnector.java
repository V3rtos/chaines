package me.moonways.bridgenet.connector.spigot;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.connector.BridgenetConnector;
import me.moonways.bridgenet.connector.BridgenetServerSync;
import me.moonways.bridgenet.connector.cloudnet.CloudnetWrapper;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.mtp.MTPMessageSender;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.logging.Logger;

public final class BridgenetSpigotConnector extends BridgenetConnector {

    @Inject
    private CloudnetWrapper cloudnetWrapper;
    @Inject
    private BeansService beansService;

    private Logger logger;

    public void start(BridgenetSpigotPlugin plugin) {
        this.logger = plugin.getLogger();
        super.doConnectBasically();

        beansService.bind(plugin);
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
            Bukkit.shutdown();
        }
    }
}
