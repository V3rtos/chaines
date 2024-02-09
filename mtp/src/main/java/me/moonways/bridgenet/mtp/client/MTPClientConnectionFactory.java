package me.moonways.bridgenet.mtp.client;

import io.netty.channel.*;
import lombok.RequiredArgsConstructor;
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

    /**
     * Создание уже подключенного клиентского канала
     * к серверу системы bridgenet по протоколу MTP.
     *
     * @param clientChannelHandler - обработчик процессов канала.
     */
    public MTPChannel newClient(MTPClientChannelHandler clientChannelHandler) {
        return connectAndChannelGet(clientChannelHandler);
    }

    /**
     * Создание уже подключенного клиентского канала
     * к серверу системы bridgenet по протоколу MTP.
     */
    public MTPChannel newClient() {
        return newClient(null);
    }

    /**
     * Создание уже подключенного клиентского канала
     * к серверу системы bridgenet по протоколу MTP.
     * Его основное отличие от обычного канала в том, что
     * данная фабрика возвращает не оригинальный канал, поэтому
     * прямого доступа к данным клиентского канала попросту не будет.
     *
     * @param clientChannelHandler - обработчик процессов канала.
     */
    public MTPMessageSender newUncastedClient(MTPClientChannelHandler clientChannelHandler) {
        return connectAndChannelWrapperGet(clientChannelHandler);
    }

    /**
     * Создание уже подключенного клиентского канала
     * к серверу системы bridgenet по протоколу MTP.
     * Его основное отличие от обычного канала в том, что
     * данная фабрика возвращает не оригинальный канал, поэтому
     * прямого доступа к данным клиентского канала попросту не будет.
     */
    public MTPMessageSender newUncastedClient() {
        return newUncastedClient(null);
    }

    /**
     * Создание уже подключенного клиентского канала к
     * серверу bridgenet в обертке.
     *
     * @param clientChannelHandler - обработчик процессов канала.
     */
    private MTPMessageSender connectAndChannelWrapperGet(MTPClientChannelHandler clientChannelHandler) {
        AtomicReference<MTPChannel> channelRef = new AtomicReference<>(connectAndChannelGet(clientChannelHandler));
        return new WrappedChannel(channelRef);
    }

    /**
     * Создание клиента протокола MTP по параметрам
     * и конфигурации, требуемой для подключения
     * к серверу bridgenet.
     *
     * @param clientChannelHandler - обработчик процессов канала.
     */
    private MTPClient createClient(MTPClientChannelHandler clientChannelHandler) {
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
        return client;
    }

    /**
     * Создание уже подключенного оригинального клиентского
     * канала к серверу bridgenet.
     *
     * @param clientChannelHandler - обработчик процессов канала.
     */
    private MTPChannel connectAndChannelGet(MTPClientChannelHandler clientChannelHandler) {
        MTPClient client = createClient(clientChannelHandler);

        MTPChannel channel = client.connect().join();
        channel.initAttributes();

        beansService.inject(channel);

        if (clientChannelHandler != null) {
            clientChannelHandler.onConnected(channel);
        }

        return channel;
    }

    @RequiredArgsConstructor
    private static class WrappedChannel implements MTPMessageSender {
        private static final long serialVersionUID = 8245149925161062394L;

        private final transient AtomicReference<MTPChannel> channelRef;

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
    }
}
