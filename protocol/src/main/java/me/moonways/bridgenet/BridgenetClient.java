package me.moonways.bridgenet;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.exception.BridgenetConnectionException;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class BridgenetClient implements BootstrapWorker {

    private final Bootstrap bootstrap;
    private final Bridgenet bridgenet;

    private BridgenetChannel bridgenetChannel;

    @Override
    public BridgenetChannel bindSync() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<BridgenetChannel> bind() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BridgenetChannel connectSync() {
        ChannelFuture channelFuture = bootstrap.connect().syncUninterruptibly();

        if (channelFuture.isSuccess()) {
            Channel channel = channelFuture.channel();
            return bridgenetChannel = new BridgenetChannel(channel, bridgenet.getMessageContainer());
        }

        throw new BridgenetConnectionException(channelFuture.cause(), "Internal synchronized connect error");
    }

    @Override
    public CompletableFuture<BridgenetChannel> connect() {
        CompletableFuture<BridgenetChannel> bridgenetChannelFuture =
                new CompletableFuture<>();

        bootstrap.connect().addListener((ChannelFutureListener) future -> {

            if (future.isSuccess()) {
                Channel channel = future.channel();
                bridgenetChannelFuture.complete(bridgenetChannel = new BridgenetChannel(channel, bridgenet.getMessageContainer()));
            }
            else {
                BridgenetConnectionException exception = new BridgenetConnectionException(future.cause(), "Internal asynchronous connect error");
                bridgenetChannelFuture.completeExceptionally(exception);
            }
        });

        return bridgenetChannelFuture;
    }

    @Override
    public void shutdownGracefully() {
        if (bridgenetChannel == null) {
            throw new BridgenetConnectionException("channel is null");
        }

        bridgenetChannel.close();
    }
}
