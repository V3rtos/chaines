package me.moonways.bridgenet.mtp.client;

import io.netty.channel.*;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.mtp.*;
import me.moonways.bridgenet.mtp.exception.ChannelException;
import me.moonways.bridgenet.mtp.pipeline.NettyPipelineInitializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

@Autobind
public class MTPClientConnectionFactory {

    @Inject
    private MTPConnectionFactory connectionFactory;
    @Inject
    private BeansService beansService;
    @Inject
    private MTPDriver driver;

    public MTPChannel newClient(MTPClientChannelHandler clientChannelHandler) {
        return connectAndChannelGet(clientChannelHandler);
    }

    public MTPChannel newClient() {
        return newClient(null);
    }

    public MTPMessageSender newUncastedClient(MTPClientChannelHandler clientChannelHandler) {
        return connectAndChannelWrapperGet(clientChannelHandler);
    }

    public MTPMessageSender newUncastedClient() {
        return newUncastedClient(null);
    }

    private MTPChannel connectAndChannelGet(MTPClientChannelHandler clientChannelHandler) {
        ChannelFactory<? extends Channel> clientChannelFactory = NettyFactory.createClientChannelFactory();

        NettyPipelineInitializer channelInitializer = NettyPipelineInitializer.create(driver, connectionFactory.getConfiguration());
        EventLoopGroup parentWorker = NettyFactory.createEventLoopGroup(2);

        MTPClient client = MTPConnectionFactory.newClientBuilder(connectionFactory)
                .setGroup(parentWorker)
                .setChannelFactory(clientChannelFactory)
                .setChannelInitializer(channelInitializer)
                .build();

        if (clientChannelHandler != null) {
            MTPClientReconnectHandler channelHandler = new MTPClientReconnectHandler(client, clientChannelHandler);
            beansService.inject(channelHandler);

            channelInitializer.addChannelHandler(channelHandler);
        }

        MTPChannel channel = client.connect().join();
        channel.initAttributes();

        beansService.inject(channel);

        if (clientChannelHandler != null) {
            clientChannelHandler.onConnected(channel);
        }

        return channel;
    }

    private MTPMessageSender connectAndChannelWrapperGet(MTPClientChannelHandler clientChannelHandler) {
        AtomicReference<MTPChannel> channelRef = new AtomicReference<>(connectAndChannelGet(clientChannelHandler));
        return new MTPMessageSender() {
            private static final long serialVersionUID = 8245149925161062394L;

            @Override
            public <T> Optional<T> getProperty(@NotNull String key) {
                return channelRef.get().getProperty(key);
            }

            @Override
            public void setProperty(@NotNull String key, @Nullable Object value) {
                channelRef.get().setProperty(key, value);
            }

            @Override
            public void sendMessage(@NotNull Object message) {
                channelRef.get().sendMessage(message);
            }

            @Override
            public <R> CompletableFuture<R> sendMessageWithResponse(@NotNull Class<R> responseType, @NotNull Object message) {
                return channelRef.get().sendMessageWithResponse(responseType, message);
            }

            @Override
            public <R> CompletableFuture<R> sendMessageWithResponse(int timeout, @NotNull Class<R> responseType, @NotNull Object message) {
                return channelRef.get().sendMessageWithResponse(timeout, responseType, message);
            }
        };
    }
}
