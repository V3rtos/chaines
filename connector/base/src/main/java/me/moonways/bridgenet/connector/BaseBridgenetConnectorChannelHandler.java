package me.moonways.bridgenet.connector;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.MTPMessageSender;
import me.moonways.bridgenet.mtp.client.MTPClientChannelHandler;
import me.moonways.bridgenet.rsi.service.RemoteServiceRegistry;

import java.util.concurrent.CompletableFuture;

@Getter
@RequiredArgsConstructor
public class BaseBridgenetConnectorChannelHandler implements MTPClientChannelHandler {

    private final BridgenetConnector connector;

    private CompletableFuture<MTPMessageSender> future;
    private MTPMessageSender channel;

    @Inject
    private RemoteServiceRegistry remoteServiceRegistry;

    private void awaitNewChannel() {
        channel = null;
        future = new CompletableFuture<>();
    }

    public MTPMessageSender getChannel() {
        if (channel == null) {
            if (future != null) {
                return future.join();
            }
        }
        return channel;
    }

    private void completeAndFlush(MTPMessageSender channel) {
        if (this.future != null) {
            this.future.complete(channel);
            this.future = null;
        }
        this.channel = channel;
    }

    @Override
    public void onConnected(MTPMessageSender channel) {
        completeAndFlush(channel);

        connector.getEngine().connectToEndpoints(remoteServiceRegistry);
        connector.onConnected(channel);
    }

    @Override
    public void onDisconnected(MTPMessageSender channel) {
        connector.getBridgenetServerSync().exportDisconnectMessage();
        connector.getEngine().disconnectToEndpoints(remoteServiceRegistry);

        this.channel = null;
    }

    @Override
    public void onReconnect(MTPMessageSender channel) {
        if (channel != null) {
            connector.handleConnection(channel);
            onConnected(channel);
        } else {
            awaitNewChannel();
        }
    }
}
