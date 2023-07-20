package me.moonways.bridgenet.mtp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.mtp.exception.ChannelException;

import java.util.concurrent.CompletableFuture;

@Log4j2
@RequiredArgsConstructor
public class MTPClient implements MTPConnection {

    private final Bootstrap bootstrap;
    private MTPChannel channel;

    private void handleChannelFuture(ChannelFuture channelFuture, CompletableFuture<MTPChannel> completableFuture) {
        if (channelFuture.isSuccess()) {

            Channel channel = channelFuture.channel();
            log.info("Successful connected to {}", channel);

            this.channel = new MTPChannel(channel);

            if (completableFuture != null) {
                completableFuture.complete(this.channel);
            }
        }
        else {
            ChannelException exception = new ChannelException(channelFuture.cause(), "Internal asynchronous connect error");
            log.error("§4Client connection proceed with exception: §c{}", exception.toString());

            if (completableFuture != null) {
                completableFuture.completeExceptionally(exception);
            }
            else {
                log.error(exception);
            }
        }
    }

    @Override
    public MTPChannel bindSync() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<MTPChannel> bind() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MTPChannel connectSync() {
        log.info("Trying synchronized connect to server netty channel");

        ChannelFuture channelFuture = bootstrap.connect().syncUninterruptibly();
        handleChannelFuture(channelFuture, null);

        return channel;
    }

    @Override
    public CompletableFuture<MTPChannel> connect() {
        log.info("Trying asynchronous connect to server netty channel");

        CompletableFuture<MTPChannel> completableFuture = new CompletableFuture<>();
        bootstrap.connect().addListener((ChannelFutureListener) future -> handleChannelFuture(future, completableFuture));

        return completableFuture;
    }

    @Override
    public void shutdownGracefully() {
        if (channel == null) {
            throw new ChannelException("channel is null");
        }

        log.info("Netty channel is shutting down");
        channel.close();
    }
}
