package me.moonways.bridgenet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.exception.BridgenetConnectionException;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Getter
public class BridgenetServer implements BootstrapWorker {

    private final ServerBootstrap serverBootstrap;
    private final Bridgenet bridgenet;

    private BridgenetChannel bridgenetChannel;

    @Override
    public BridgenetChannel bindSync() {
        ChannelFuture channelFuture = serverBootstrap.bind().syncUninterruptibly();

        if (channelFuture.isSuccess()) {
            Channel channel = channelFuture.channel();

            System.out.println("channel was initialized");
            return bridgenetChannel = new BridgenetChannel(channel, bridgenet.getMessageContainer());
        }

        throw new BridgenetConnectionException(channelFuture.cause(), "Internal synchronized bind error");
    }

    @Override
    public CompletableFuture<BridgenetChannel> bind() {
        CompletableFuture<BridgenetChannel> bridgenetChannelFuture =
                new CompletableFuture<>();

        serverBootstrap.bind().addListener((ChannelFutureListener) future -> {

            if (future.isSuccess()) {
                Channel channel = future.channel();
                bridgenetChannelFuture.complete(bridgenetChannel = new BridgenetChannel(channel, bridgenet.getMessageContainer()));

                System.out.println("channel was initialized");
            }
            else {
                BridgenetConnectionException exception = new BridgenetConnectionException(future.cause(), "Internal asynchronous bind error");
                bridgenetChannelFuture.completeExceptionally(exception);

                System.out.println("channel throws exception");
            }
        });

        return bridgenetChannelFuture;
    }

    @Override
    public BridgenetChannel connectSync() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<BridgenetChannel> connect() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shutdownGracefully() {
        if (bridgenetChannel == null) {
            throw new BridgenetConnectionException("channel is null");
        }

        bridgenetChannel.close();
    }
}
