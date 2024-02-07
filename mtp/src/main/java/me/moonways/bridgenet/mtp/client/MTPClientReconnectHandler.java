package me.moonways.bridgenet.mtp.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.util.thread.Threads;
import me.moonways.bridgenet.mtp.MTPChannel;
import me.moonways.bridgenet.mtp.MTPClient;
import me.moonways.bridgenet.mtp.exception.ChannelException;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class MTPClientReconnectHandler extends ChannelInboundHandlerAdapter {

    private final MTPClient client;
    private final ScheduledExecutorService reconnectScheduledExecutor = Threads.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> currentReconnectTaskFuture;

    private final MTPClientChannelHandler clientHandler;

    @Inject
    private BeansService beansService;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (currentReconnectTaskFuture == null) {
            clientHandler.onDisconnected(client.getChannel());
        }
        startReconnect();
    }

    private synchronized void startReconnect() {
        if (currentReconnectTaskFuture != null && !currentReconnectTaskFuture.isCancelled()) {
            throw new UnsupportedOperationException("reconnect operation is not cancelled");
        }

        clientHandler.onReconnect(null);
        currentReconnectTaskFuture = reconnectScheduledExecutor.schedule(() -> {
            if (tryReconnect()) {
                completeReconnect();
            }

        }, 10, TimeUnit.SECONDS);
    }

    private synchronized void completeReconnect() {
        if (currentReconnectTaskFuture == null) {
            throw new UnsupportedOperationException("reconnect operation is not exists");
        }

        currentReconnectTaskFuture.cancel(true);
        currentReconnectTaskFuture = null;
    }

    private synchronized boolean tryReconnect() {
        try {
            MTPChannel channel = client.connectSync();

            beansService.inject(channel);
            clientHandler.onReconnect(channel); // обновляем канал коннектора на только что подключенный

            return true;
        } catch (ChannelException exception) {
           return false; // ошибку выкидывать незачем, просто пробуем переподключиться вновь
        }
    }
}
