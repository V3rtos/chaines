package me.moonways.bridgenet.protocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.protocol.exception.BridgenetConnectionException;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Log4j2
public class BridgenetClient implements BootstrapWorker {

    private final Bootstrap bootstrap;
    private final ProtocolControl protocolControl;

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

            log.info(String.format("Successful synchronous connected to server: %s", channel));

            return bridgenetChannel = new BridgenetChannel(channel, protocolControl);
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
                bridgenetChannelFuture.complete(bridgenetChannel = new BridgenetChannel(channel, protocolControl));

                log.info(String.format("Successful connected to server: %s", channel));
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
