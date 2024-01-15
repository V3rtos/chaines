package me.moonways.bridgenet.mtp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.util.thread.Threads;
import me.moonways.bridgenet.mtp.exception.ChannelException;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Getter
@Log4j2
public class MTPServer implements MTPConnection {

    private final ServerBootstrap serverBootstrap;

    private MTPChannel channel;

    private void handleChannelFuture(ChannelFuture channelFuture, CompletableFuture<MTPChannel> completableFuture) {
        if (channelFuture.isSuccess()) {

            Channel channel = channelFuture.channel();
            log.info("Successful bind server {}", channel);

            this.channel = new MTPChannel(false, channel);
            Threads.hookShutdown(channel::close);

            if (completableFuture != null) {
                completableFuture.complete(this.channel);
            }
        }
        else {
            Throwable cause = channelFuture.cause();
            log.error("§4Server bind proceed with exception: §c{}", cause.toString());

            if (completableFuture != null) {
                completableFuture.completeExceptionally(cause);
            }
            else {
                log.error("Internal asynchronous bind error", cause);
            }
        }
    }

    @Override
    public MTPChannel bindSync() {
        ChannelFuture channelFuture = serverBootstrap.bind().syncUninterruptibly();
        handleChannelFuture(channelFuture, null);

        return channel;
    }

    @Override
    public CompletableFuture<MTPChannel> bind() {
        CompletableFuture<MTPChannel> completableFuture = new CompletableFuture<>();
        serverBootstrap.bind().addListener((ChannelFutureListener) future -> handleChannelFuture(future, completableFuture));

        return completableFuture;
    }

    @Override
    public MTPChannel connectSync() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<MTPChannel> connect() {
        throw new UnsupportedOperationException();
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
