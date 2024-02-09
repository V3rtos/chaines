package me.moonways.bridgenet.test.connector.subj;

import me.moonways.bridgenet.connector.BridgenetConnector;
import me.moonways.bridgenet.connector.BridgenetServerSync;
import me.moonways.bridgenet.connector.DeviceDescription;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.mtp.MTPMessageSender;

import java.util.List;

public class TestConnector extends BridgenetConnector {

    public static final DeviceDescription DEVICE_DESCRIPTION = DeviceDescription.builder()
            .name("BungeeCord-1")
            .host("127.0.0.1")
            .port(25565)
            .build();

    @Override
    protected DeviceDescription createDescription() {
        return DEVICE_DESCRIPTION;
    }

    @Override
    public void onConnected(MTPMessageSender channel) {
        System.out.println("SUCCESSFUL CONNECTED TO BRIDGENET SERVER!");
    }

    public Handshake.Result retryHandshakeExchanging() {
        BridgenetServerSync bridgenet = getBridgenetServerSync();
        return bridgenet.exchangeHandshake(
                TestConnector.DEVICE_DESCRIPTION.getName(),
                TestConnector.DEVICE_DESCRIPTION.getHost(),
                TestConnector.DEVICE_DESCRIPTION.getPort());
    }

    public List<String> lookupBridgenetRegisteredComamndsList() {
        BridgenetServerSync bridgenet = getBridgenetServerSync();
        return bridgenet.lookupBridgenetServerCommandsList();
    }
}