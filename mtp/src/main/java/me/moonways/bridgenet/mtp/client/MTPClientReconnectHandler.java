package me.moonways.bridgenet.mtp.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.util.thread.Threads;
import me.moonways.bridgenet.mtp.MTPChannel;
import me.moonways.bridgenet.mtp.MTPClient;
import me.moonways.bridgenet.mtp.exception.ChannelException;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Log4j2
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

        log.warn("§4Bridgenet server channel has inactive, trying reconnect...");
        startReconnect();
    }

    private synchronized void startReconnect() {
        if (currentReconnectTaskFuture != null && !currentReconnectTaskFuture.isCancelled()) {
            throw new UnsupportedOperationException("reconnect operation is not cancelled");
        }

        clientHandler.onReconnect(null);
        currentReconnectTaskFuture = reconnectScheduledExecutor.scheduleAtFixedRate(() -> {
            if (tryReconnect()) {
                completeReconnect();
            }

        }, 3, 5, TimeUnit.SECONDS);
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
            MTPChannel channel = client.connect().join();

            beansService.inject(channel);
            clientHandler.onReconnect(channel); // обновляем канал коннектора на только что подключенный

            log.info("§2Reconnection attempt is successful!");
            return true;
        } catch (Exception exception) {
            log.error("§4Reconnection attempt is failed, try again: {}", exception.toString());
            return false; // ошибку выкидывать незачем, просто пробуем переподключиться вновь
        }
    }
}
