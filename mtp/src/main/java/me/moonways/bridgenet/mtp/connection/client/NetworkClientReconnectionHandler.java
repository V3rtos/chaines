package me.moonways.bridgenet.mtp.connection.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.util.thread.Threads;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import me.moonways.bridgenet.mtp.connection.BridgenetNetworkConnection;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2
@RequiredArgsConstructor
public class NetworkClientReconnectionHandler extends ChannelInboundHandlerAdapter {

    public static final String CHANNEL_ATTRIBUTE_NAME = "bridgenet-client-reconnection";

    private final ScheduledExecutorService reconnectScheduledExecutor = Threads.newSingleThreadScheduledExecutor();

    private final BridgenetNetworkConnection client;
    private final BridgenetNetworkClientHandler clientHandler;

    private boolean isRunning;

    private CompletableFuture<BridgenetNetworkChannel> futureState;

    @Inject
    private BeansService beansService;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (!isRunning) {
            clientHandler.onDisconnected(client.getChannel());
        }

        log.warn("§6Bridgenet server channel has inactive, trying reconnect...");
        startReconnect();
    }

    private synchronized void startReconnect() {
        isRunning = true;

        clientHandler.onReconnect(null);
        reconnectScheduledExecutor.schedule(this::tryReconnect, 5, TimeUnit.SECONDS);
    }

    private synchronized void tryReconnect() {
        try {
            BridgenetNetworkChannel channel = client.connect().get(1, TimeUnit.SECONDS);
            clientHandler.onReconnect(channel); // обновляем канал коннектора на только что подключенный

            isRunning = false;

            if (futureState != null) {
                futureState.complete(channel);
            }

            log.info("§2Reconnection attempt is successful!");
        } catch (Exception exception) {

            if (futureState != null) {
                futureState.complete(null);
            }

            log.error("§4Reconnection attempt is failed, try again: §c{}", exception.toString());
        }
    }

    public CompletableFuture<BridgenetNetworkChannel> transitFuture() {
        return futureState = new CompletableFuture<>();
    }
}
