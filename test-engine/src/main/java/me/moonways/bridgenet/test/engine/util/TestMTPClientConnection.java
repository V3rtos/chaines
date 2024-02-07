package me.moonways.bridgenet.test.engine.util;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.mtp.client.MTPClientConnectionFactory;
import me.moonways.bridgenet.mtp.MTPMessageSender;

@Autobind
public class TestMTPClientConnection {

    @Inject
    private BeansService beansService;

    private MTPMessageSender channel;
    private MTPClientConnectionFactory clientConnectionFactory;

    public MTPMessageSender getChannel() {
        if (clientConnectionFactory == null) {
            clientConnectionFactory = new MTPClientConnectionFactory();
            beansService.inject(clientConnectionFactory);
        }
        if (channel == null) {
            channel = clientConnectionFactory.newClient();
        }
        return channel;
    }
}
