package me.moonways.bridgenet.test.engine.objects.mtp;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.mtp.connection.client.NetworkClientConnectionFactory;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;

@Autobind
public class TestMTPClientConnection {

    @Inject
    private BeansService beansService;

    private BridgenetNetworkChannel channel;
    private NetworkClientConnectionFactory clientConnectionFactory;

    public BridgenetNetworkChannel getChannel() {
        if (clientConnectionFactory == null) {
            clientConnectionFactory = new NetworkClientConnectionFactory();
            beansService.inject(clientConnectionFactory);
        }
        if (channel == null) {
            channel = clientConnectionFactory.newRemoteClient();
        }
        return channel;
    }
}
