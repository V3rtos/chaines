package me.moonways.bridgenet.connector.spigot;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.connector.BridgenetConnector;
import me.moonways.bridgenet.connector.ConnectedDeviceInfo;
import me.moonways.bridgenet.connector.cloudnet.CloudnetWrapper;
import me.moonways.bridgenet.model.bus.message.Handshake;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public final class BridgenetSpigotConnector extends BridgenetConnector {

    @Inject
    private BeansService beansService;

    private final BridgenetSpigotPlugin plugin;

    @Override
    protected ConnectedDeviceInfo createDeviceInfo() {
        CloudnetWrapper cloudnetWrapper = new CloudnetWrapper();
        beansService.bind(cloudnetWrapper);

        return ConnectedDeviceInfo.builder()
                .name(cloudnetWrapper.getFullCurrentServiceName())
                .host(cloudnetWrapper.getCurrentSnapshotHost())
                .port(cloudnetWrapper.getCurrentSnapshotPort())
                .build();
    }

    @Override
    public void onHandshake(Handshake.Result result) {
        result.onSuccess(() -> beansService.bind(plugin));
        result.onFailure(() -> {

            plugin.getLogger().info("§4Handshake failed: Server has already registered by " + result.getKey());
            Bukkit.shutdown();
        });
    }
}
