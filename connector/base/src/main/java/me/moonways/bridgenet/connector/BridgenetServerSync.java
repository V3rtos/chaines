package me.moonways.bridgenet.connector;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.moonways.bridgenet.connector.description.DeviceDescription;
import me.moonways.bridgenet.connector.description.UserDescription;
import me.moonways.bridgenet.model.bus.message.Disconnect;
import me.moonways.bridgenet.model.bus.message.GetCommands;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.model.bus.message.SendCommand;
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
     * @param description - описание подключаемого устройства.
     */
    private static Properties prepareDeviceHandshakeProperties(DeviceDescription description) {
        Properties properties = new Properties();
        properties.setProperty("server.name", description.getName());
        properties.setProperty("server.address.host", description.getHost());
        properties.setProperty("server.address.port", Integer.toString(description.getPort()));
        return properties;
    }

    /**
     * Подготовка конфигурации сообщения о рукопожатии
     * пользователя с единым сервером Bridgenet.
     *
     * @param description - описание подключаемого пользователя.
     */
    private static Properties prepareUserHandshakeProperties(UserDescription description) {
        Properties properties = new Properties();
        properties.setProperty("user.name", description.getName());
        properties.setProperty("user.uuid", description.getUniqueId().toString());
        properties.setProperty("user.proxy", description.getProxyUuid().toString());
        return properties;
    }

    /**
     * Выполнить обмен данными с единым сервером Bridgenet при
     * помощи рукопожатия и получить в результате уникальный идентификатор
     * текущего устройства.
     *
     * @param description - описание подключаемого устройства.
     */
    public Handshake.Result exchangeHandshake(DeviceDescription description) {
        CompletableFuture<Handshake.Result> completableFuture = channel.sendMessageWithResponse(Handshake.Result.class,
                new Handshake(Handshake.Type.SERVER,
                        prepareDeviceHandshakeProperties(description)));

        Handshake.Result result = completableFuture.join();
        result.onSuccess(() -> serverUuid = result.getKey());

        return result;
    }

    /**
     * Отправить сообщение об отсоединении текущего устройства
     */
    public void exportDisconnectMessage() {
        if (serverUuid != null) {
            channel.sendMessage(new Disconnect(serverUuid, Disconnect.Type.SERVER));
        }
    }

    /**
     * Выполнить обмен данными подключаемого
     * пользователя к единому серверу Bridgenet
     * при помощи рукопожатия и получить в результате
     * уникальный идентификатор пользователя
     * в качестве подтверждения его инициализации.
     *
     * @param description - описание подключаемого пользователя.
     * @return - возвращает TRUE если вернувшийся идентификатор совпадает с пользовательским.
     */
    public boolean exportUserHandshake(UserDescription description) {
        CompletableFuture<Handshake.Result> completableFuture = channel.sendMessageWithResponse(Handshake.Result.class,
                new Handshake(Handshake.Type.PLAYER,
                        prepareUserHandshakeProperties(description)));

        Handshake.Result result = completableFuture.join();

        if (result instanceof Handshake.Success) {
            return result.getKey().toString().equals(description.getUniqueId().toString());
        }

        return false;
    }

    /**
     * Отправить сообщение об отсоединении пользователя.
     * @param description - описание подключаемого пользователя.
     */
    public void exportUserDisconnect(UserDescription description) {
        channel.sendMessage(new Disconnect(description.getUniqueId(), Disconnect.Type.PLAYER));
    }

    /**
     * Экспортировать исполнение команды на единый
     * сервер Bridgenet и делегировать данную ответственность
     * на него.
     *
     * @param description - описание пользователя.
     * @param label - введенная пользователем строка команды.
     *
     * @return - возвращает TRUE если команда была успешно исполнена.
     */
    public boolean exportSendCommand(UserDescription description, String label) {
        CompletableFuture<SendCommand.Result> future = channel.sendMessageWithResponse(SendCommand.Result.class,
                new SendCommand(description.getUniqueId(), label));

        SendCommand.Result commandSendResult = future.join();
        return (commandSendResult instanceof SendCommand.Success);
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
