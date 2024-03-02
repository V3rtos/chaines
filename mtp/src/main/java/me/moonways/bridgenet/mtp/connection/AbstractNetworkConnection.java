package me.moonways.bridgenet.mtp.connection;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.util.thread.Threads;
import me.moonways.bridgenet.mtp.BridgenetChannelException;
import me.moonways.bridgenet.mtp.BridgenetNetworkController;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import me.moonways.bridgenet.mtp.connection.client.NetworkClientReconnectionHandler;

import java.util.concurrent.CompletableFuture;

@Log4j2
@RequiredArgsConstructor
public abstract class AbstractNetworkConnection implements BridgenetNetworkConnection {

    private final ServerBootstrap serverBootstrap;
    private final Bootstrap clientBootstrap;

    @Getter
    private BridgenetNetworkChannel channel;

    @Inject
    private BridgenetNetworkController networkController;

    protected abstract BridgenetNetworkChannel prepareChannelSuccess(Channel channel);

    @Override
    public BridgenetNetworkChannel bindSync() {
        ChannelFuture channelFuture = serverBootstrap.bind().syncUninterruptibly();
        handleFuture(channelFuture, null);

        return channel;
    }

    @Override
    public CompletableFuture<BridgenetNetworkChannel> bind() {
        CompletableFuture<BridgenetNetworkChannel> completableFuture = new CompletableFuture<>();
        serverBootstrap.bind().addListener((ChannelFutureListener) future -> handleFuture(future, completableFuture));

        return completableFuture;
    }

    @Override
    public BridgenetNetworkChannel connectSync() {
        ChannelFuture channelFuture = clientBootstrap.connect().syncUninterruptibly();
        handleFuture(channelFuture, null);

        return channel;
    }

    @Override
    public CompletableFuture<BridgenetNetworkChannel> connect() {
        CompletableFuture<BridgenetNetworkChannel> completableFuture = new CompletableFuture<>();
        clientBootstrap.connect().addListener((ChannelFutureListener) future -> handleFuture(future, completableFuture));

        return completableFuture;
    }

    private void handleFuture(ChannelFuture future, CompletableFuture<BridgenetNetworkChannel> completableFuture) {
        Channel channel = future.channel();

        if (future.isSuccess()) {
            handleSuccessFuture(channel, completableFuture);
        } else {
            Throwable cause = future.cause();
            handleFailureFuture(cause, channel, completableFuture);
        }
    }

    private void handleSuccessFuture(Channel nettyChannel, CompletableFuture<BridgenetNetworkChannel> future) {
        log.info("Channel {} has been successfully prepared for operation", nettyChannel);

        channel = prepareChannelSuccess(nettyChannel);
        Threads.hookShutdown(nettyChannel::close);

        if (future != null) {
            future.complete(channel);
        }
    }

    private void handleFailureFuture(Throwable cause, Channel channel, CompletableFuture<BridgenetNetworkChannel> future) {
        log.error("§4Channel {} gave an error during preparation: §c{}", channel, cause.toString(), cause);

        if (future != null) {
            channel.pipeline().fireChannelInactive();

            // if connection is client then getting channel from reconnection-handler
            if (clientBootstrap != null) {
                tryClientReconnectOnFailure(channel, future);
            }
            // or throw future exception just.
            else {
                future.completeExceptionally(cause);
            }
        }
    }

    private void tryClientReconnectOnFailure(Channel nettyChannel, CompletableFuture<BridgenetNetworkChannel> future) {
        NetworkClientReconnectionHandler reconnectionHandler = (NetworkClientReconnectionHandler)
                nettyChannel.attr(AttributeKey.valueOf(NetworkClientReconnectionHandler.CHANNEL_ATTRIBUTE_NAME)).get();

        BridgenetNetworkChannel reconnectedChannel = reconnectionHandler.transitFuture().join();

        if (reconnectedChannel != null) {
            future.complete(reconnectedChannel);
        }
    }

    @Override
    public final void shutdown() {
        if (channel == null) {
            throw new BridgenetChannelException("null");
        }

        log.info("io.netty.channel.Channel is shutting down");
        channel.close();
    }
}
