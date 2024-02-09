package me.moonways.bridgenet.connector;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.moonways.bridgenet.model.bus.message.Disconnect;
import me.moonways.bridgenet.model.bus.message.GetCommands;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.mtp.MTPMessageSender;

import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class BridgenetServerSync {

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private MTPMessageSender channel;
    @Getter
    private UUID serverUuid;

    /**
     * Подготовка конфигурации сообщения о рукопожатии с
     * единым сервером Bridgenet.
     *
     * @param serverName - имя подключаемого сервера.
     * @param serverHost - адрес подключаемого сервера.
     * @param serverPort - порт подключаемого сервера.
     */
    private static Properties prepareHandshakeProperties(String serverName, String serverHost, int serverPort) {
        Properties properties = new Properties();

        properties.setProperty("server.name", serverName);
        properties.setProperty("server.address.host", serverHost);
        properties.setProperty("server.address.port", Integer.toString(serverPort));
        return properties;
    }

    /**
     * Выполнить обмен данными с единым сервером Bridgenet при
     * помощи рукопожатия и получить в результате уникальный идентификатор
     * текущего устройства.
     *
     * @param serverName - имя подключаемого сервера.
     * @param serverHost - адрес подключаемого сервера.
     * @param serverPort - порт подключаемого сервера.
     */
    public Handshake.Result sendServerHandshake(String serverName, String serverHost, int serverPort) {
        CompletableFuture<Handshake.Result> completableFuture = channel.sendMessageWithResponse(Handshake.Result.class,
                new Handshake(Handshake.Type.SERVER,
                        prepareHandshakeProperties(serverName, serverHost, serverPort)));

        Handshake.Result result = completableFuture.join();
        serverUuid = result.getKey();

        return result;
    }

    /**
     * Отправить сообщение об отсоединении текущего устройства
     */
    public void sendServerDisconnect() {
        if (serverUuid != null) {
            channel.sendMessage(new Disconnect(serverUuid, Disconnect.Type.SERVER));
        }
    }

    /**
     * Запросить у единого сервера Bridgenet список команд,
     * зарегистрированных в нем для возможности исполнения на данном устройстве.
     */
    public List<String> lookupBridgenetServerCommandsList() {
        CompletableFuture<GetCommands.Result> future
                = channel.sendMessageWithResponse(GetCommands.Result.class, new GetCommands());

        GetCommands.Result result = future.join();
        return result.getList();
    }
}
