package me.moonways.bridgenet.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.protocol.exception.BridgenetConnectionException;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Getter
@Log4j2
public class BridgenetServer implements BootstrapWorker {

    private final ServerBootstrap serverBootstrap;
    private final ProtocolControl protocolControl;

    private BridgenetChannel bridgenetChannel;

    @Override
    public BridgenetChannel bindSync() {
        ChannelFuture channelFuture = serverBootstrap.bind().syncUninterruptibly();

        if (channelFuture.isSuccess()) {
            Channel channel = channelFuture.channel();

            log.info(String.format("Successful synchronous bind server: %s", channel));

            return bridgenetChannel = new BridgenetChannel(channel, protocolControl);
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
                bridgenetChannelFuture.complete(bridgenetChannel = new BridgenetChannel(channel, protocolControl));

                log.info(String.format("Successful bind server: %s", channel));
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
