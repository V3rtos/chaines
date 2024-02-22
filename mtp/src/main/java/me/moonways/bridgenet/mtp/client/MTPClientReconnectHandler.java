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

    private final MTPClientChannelHandler clientHandler;

    private boolean isRunning;

    @Inject
    private BeansService beansService;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (!isRunning) {
            clientHandler.onDisconnected(client.getChannel());
        }

        log.warn("§4Bridgenet server channel has inactive, trying reconnect...");
        startReconnect();
    }

    private synchronized void startReconnect() {
        isRunning = true;

        clientHandler.onReconnect(null);
        reconnectScheduledExecutor.schedule(this::tryReconnect, 5, TimeUnit.SECONDS);
    }

    private synchronized void tryReconnect() {
        try {
            MTPChannel channel = client.connect().join();

            beansService.inject(channel);
            clientHandler.onReconnect(channel); // обновляем канал коннектора на только что подключенный

            isRunning = false;

            log.info("§2Reconnection attempt is successful!");
        } catch (Exception exception) {
            log.error("§4Reconnection attempt is failed, try again: {}", exception.toString());
        }
    }
}
