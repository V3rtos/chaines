package me.moonways.bridgenet.test.data;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.client.api.BridgenetClient;
import me.moonways.bridgenet.client.api.BridgenetServerSync;
import me.moonways.bridgenet.client.api.data.ClientDto;
import me.moonways.bridgenet.model.message.Handshake;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;

import java.util.List;

@Log4j2
public class ExampleClient extends BridgenetClient {

    @Override
    protected ClientDto createClientInfo() {
        return TestConst.Server.DESC;
    }

    @Override
    public void onConnected(BridgenetNetworkChannel channel) {
        log.info("§d§nSUCCESSFUL CONNECTED TO BRIDGENET SERVER!");
    }

    @Override
    public void onHandshake(Handshake.Result result) {
        result.onSuccess(() -> log.info("§d§nHANDSHAKE SUCCESS EXCHANGED"));
        result.onFailure(() -> log.info("§d§nHANDSHAKE HAS FAILED"));
    }

    public Handshake.Result retryHandshakeExchanging() {
        BridgenetServerSync bridgenet = getBridgenetServerSync();
        return bridgenet.exportClientHandshake(createClientInfo());
    }

    public List<String> lookupBridgenetRegisteredComamndsList() {
        BridgenetServerSync bridgenet = getBridgenetServerSync();
        return bridgenet.lookupServerCommandsList();
    }
}