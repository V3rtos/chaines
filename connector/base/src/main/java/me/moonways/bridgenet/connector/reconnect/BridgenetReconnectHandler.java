package me.moonways.bridgenet.connector.reconnect;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.connector.BaseBridgenetConnector;
import me.moonways.bridgenet.connector.exception.BridgenetConnectionClosedException;
import me.moonways.bridgenet.protocol.BridgenetChannel;
import me.moonways.bridgenet.protocol.BridgenetClient;
import me.moonways.bridgenet.protocol.exception.BridgenetConnectionException;

import java.util.concurrent.*;

@RequiredArgsConstructor
public class BridgenetReconnectHandler extends ChannelInboundHandlerAdapter {

    private final BridgenetClient bridgenetClient;
    private final BaseBridgenetConnector bridgenetConnector;

    private final ScheduledExecutorService reconnectScheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> currentReconnectTaskFuture;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        try {
            startReconnect();
        }
        catch (BridgenetConnectionClosedException exception) {
            exception.printStackTrace();
        }
    }

    private synchronized void startReconnect() {
        if (currentReconnectTaskFuture != null && !currentReconnectTaskFuture.isCancelled()) {
            throw new UnsupportedOperationException("reconnect operation is not cancelled");
        }

        bridgenetConnector.setChannel(null);
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
            BridgenetChannel bridgenetChannel = bridgenetClient.connectSync();
            bridgenetConnector.setChannel(bridgenetChannel); // обновляем канал коннектора на только что подключенный

            return true;
        } catch (BridgenetConnectionException exception) {
           return false; // ошибку выкидывать незачем, просто пробуем переподключиться вновь
        }
    }
}
