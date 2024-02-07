package me.moonways.bridgenet.connector;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.mtp.MTPMessageSender;
import me.moonways.bridgenet.mtp.client.MTPClientChannelHandler;

import java.util.concurrent.CompletableFuture;

@Getter
@RequiredArgsConstructor
public class BaseBridgenetConnectorChannelHandler implements MTPClientChannelHandler {

    private CompletableFuture<MTPMessageSender> future;
    private MTPMessageSender channel;

    private MTPMessageSender awaitNewChannel() {
        future = new CompletableFuture<>();
        channel = null;

        return channel = future.join();
    }

    public MTPMessageSender getChannel() {
        if (channel == null) {
            channel = awaitNewChannel();
        }
        return channel;
    }

    private void completeAndFlush(MTPMessageSender channel) {
        if (this.future != null) {
            this.future.complete(channel);
            this.future = null;
        } else {
            this.channel = channel;
        }
    }

    @Override
    public void onConnected(MTPMessageSender channel) {
        completeAndFlush(channel);
    }

    @Override
    public void onDisconnected(MTPMessageSender channel) {
        this.channel = null;
    }

    @Override
    public void onReconnect(MTPMessageSender channel) {
        if (channel != null) {
            completeAndFlush(channel);
        } else {
            this.channel = awaitNewChannel();
        }
    }
}
