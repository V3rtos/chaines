package me.moonways.bridgenet.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import me.moonways.bridgenet.connector.BridgenetConnector;
import me.moonways.bridgenet.protocol.Bridgenet;
import me.moonways.bridgenet.service.inject.Component;
import me.moonways.bridgenet.service.inject.Inject;

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

    private final Bridgenet bridgenet = Bridgenet.createByProperties();

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }
}
