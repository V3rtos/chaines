package me.moonways.bridgenet.reconnect;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.exception.BridgenetConnectionClosedException;
import me.moonways.bridgenet.protocol.BridgenetClient;
import me.moonways.bridgenet.protocol.exception.BridgenetConnectionException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class BridgenetReconnectHandler extends ChannelInboundHandlerAdapter {

    private final BridgenetClient bridgenetClient;

    private final ScheduledExecutorService reconnectScheduler = new ScheduledThreadPoolExecutor(1);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        try {
            startReconnect();
        } catch (BridgenetConnectionClosedException exception) {
            exception.printStackTrace();
        }
    }

    private synchronized void startReconnect() {
        reconnectScheduler.schedule(() -> {
            tryReconnect().whenComplete((value, throwable) -> {
                if (throwable != null) {
                    throwable.printStackTrace();
                }

                if (value) {
                    completeReconnect();
                }
            });
        }, 10L, TimeUnit.SECONDS);
    }

    private synchronized void completeReconnect() {
        reconnectScheduler.shutdown();
    }

    private synchronized CompletableFuture<Boolean> tryReconnect() {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        try {
            bridgenetClient.connectSync();
        } catch (BridgenetConnectionException exception) {
            completableFuture.completeExceptionally(new BridgenetConnectionClosedException(exception, "bridgenet try connection was lost"));
        }

        completableFuture.complete(true);

        return completableFuture;
    }
}
