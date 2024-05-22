package me.moonways.bridgenet.mtp.connection.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.HiddenRuntimeException;
import me.moonways.bridgenet.mtp.BridgenetNetworkController;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import me.moonways.bridgenet.mtp.channel.ChannelDirection;
import me.moonways.bridgenet.mtp.channel.NetworkReferenceChannel;
import me.moonways.bridgenet.mtp.connection.BridgenetNetworkConnection;
import me.moonways.bridgenet.mtp.connection.BridgenetNetworkConnectionFactory;
import me.moonways.bridgenet.mtp.connection.NetworkBootstrapFactory;
import me.moonways.bridgenet.mtp.inbound.InboundChannelOptionsHandler;

import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicReference;

@Autobind
public class NetworkClientConnectionFactory {

    @Inject
    private BridgenetNetworkConnectionFactory networkConnectionFactory;
    @Inject
    private BridgenetNetworkController networkController;
    @Inject
    private InboundChannelOptionsHandler inboundChannelOptionsHandler;

    /**
     * Создание уже подключенного клиентского канала
     * к серверу системы bridgenet по протоколу MTP.
     *
     * @param clientChannelHandler - обработчик процессов канала.
     */
    public BridgenetNetworkChannel newRemoteClient(BridgenetNetworkClientHandler clientChannelHandler) {
        return connectAndChannelGet(clientChannelHandler);
    }

    /**
     * Создание уже подключенного клиентского канала
     * к серверу системы bridgenet по протоколу MTP.
     */
    public BridgenetNetworkChannel newRemoteClient() {
        return newRemoteClient(null);
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
    public BridgenetNetworkChannel newReferenceClient(BridgenetNetworkClientHandler clientChannelHandler) {
        return connectAndChannelWrapperGet(clientChannelHandler);
    }

    /**
     * Создание уже подключенного клиентского канала
     * к серверу системы bridgenet по протоколу MTP.
     * Его основное отличие от обычного канала в том, что
     * данная фабрика возвращает не оригинальный канал, поэтому
     * прямого доступа к данным клиентского канала попросту не будет.
     */
    public BridgenetNetworkChannel newReferenceClient() {
        return newReferenceClient(null);
    }

    /**
     * Создание уже подключенного клиентского канала к
     * серверу bridgenet в обертке.
     *
     * @param clientChannelHandler - обработчик процессов канала.
     */
    private BridgenetNetworkChannel connectAndChannelWrapperGet(BridgenetNetworkClientHandler clientChannelHandler) {
        AtomicReference<BridgenetNetworkChannel> channelRef = new AtomicReference<>(connectAndChannelGet(clientChannelHandler));
        return new NetworkReferenceChannel(channelRef);
    }

    /**
     * Создание клиента протокола MTP по параметрам
     * и конфигурации, требуемой для подключения
     * к серверу bridgenet.
     *
     * @param networkClientHandler - обработчик процессов клиентского канала.
     */
    private BridgenetNetworkConnection createClient(BridgenetNetworkClientHandler networkClientHandler) {
        ChannelFactory<? extends Channel> clientChannelFactory = NetworkBootstrapFactory.createClientChannelFactory();
        EventLoopGroup parentWorker = NetworkBootstrapFactory.createEventLoopGroup(2);

        inboundChannelOptionsHandler.setChannelDirection(ChannelDirection.TO_SERVER);
        inboundChannelOptionsHandler.thenComplete(channel -> {

            Attribute<Object> attr = channel.attr(AttributeKey.valueOf(BridgenetNetworkClientHandler.ATTRIBUTE_KEY));
            attr.set(networkClientHandler);
        });

        BridgenetNetworkConnection client = networkConnectionFactory.newClientBuilder()
                .setGroup(parentWorker)
                .setChannelFactory(clientChannelFactory)
                .setChannelInitializer(inboundChannelOptionsHandler)
                .build();

        // Add reconnection to server handler as children handler.
        if (networkClientHandler != null) {
            NetworkClientReconnectionHandler reconnectionHandler = networkConnectionFactory.newClientReconnectionHandler(client, networkClientHandler);

            inboundChannelOptionsHandler.addChannelHandler(reconnectionHandler);
            inboundChannelOptionsHandler.thenComplete(channel ->
                    channel.attr(AttributeKey.valueOf(NetworkClientReconnectionHandler.CHANNEL_ATTRIBUTE_NAME)).set(reconnectionHandler));
        }

        return client;
    }

    /**
     * Создание уже подключенного оригинального клиентского
     * канала к серверу bridgenet.
     *
     * @param clientChannelHandler - обработчик процессов канала.
     */
    private BridgenetNetworkChannel connectAndChannelGet(BridgenetNetworkClientHandler clientChannelHandler) {
        BridgenetNetworkConnection client = createClient(clientChannelHandler);
        try {
            BridgenetNetworkChannel channel = client.connect().join();

            if (clientChannelHandler != null) {
                clientChannelHandler.onConnected(channel);
            }

            return channel;
        } catch (CompletionException exception) {
            if (networkController.isAnnotatedConnectException(exception.getCause())) {
                throw new HiddenRuntimeException();
            }
            throw exception;
        }
    }
}
