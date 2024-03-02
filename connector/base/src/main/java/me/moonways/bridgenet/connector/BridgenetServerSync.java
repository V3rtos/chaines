package me.moonways.bridgenet.connector;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.moonways.bridgenet.connector.description.*;
import me.moonways.bridgenet.model.bus.message.*;
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
    private UUID currentDeviceId;

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
    public Handshake.Result exportDeviceHandshake(DeviceDescription description) {
        CompletableFuture<Handshake.Result> future = channel.sendMessageWithResponse(Handshake.Result.class,
                new Handshake(Handshake.Type.SERVER,
                        prepareDeviceHandshakeProperties(description)));

        Handshake.Result result = future.join();
        result.onSuccess(() -> currentDeviceId = result.getKey());

        return result;
    }

    /**
     * Выполнить обмен данными для создания игры с
     * единым сервером Bridgenet.
     *
     * @param description - описание подключаемой игры.
     */
    public CreateGame.Result exportGameCreate(GameDescription description) {
        CompletableFuture<CreateGame.Result> future = channel.sendMessageWithResponse(CreateGame.Result.class,
                new CreateGame(
                        description.getName(),
                        description.getMap(),
                        description.getMaxPlayers(),
                        description.getPlayersInTeam()));
        return future.join();
    }

    /**
     * Экспортировать обновление данных одной из активных игр.
     * @param description - описание подключаемой игры.
     */
    public void exportGameUpdate(GameStateDescription description) {
        ActiveGameDescription activeGame = description.getActiveGame();
        channel.sendMessage(new UpdateGame(activeGame.getGameId(), activeGame.getActiveId(),
                description.getStatus(),
                description.getSpectators(),
                description.getPlayers()));
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
    public boolean exportCommandSend(UserDescription description, String label) {
        CompletableFuture<SendCommand.Result> future = channel.sendMessageWithResponse(SendCommand.Result.class,
                new SendCommand(description.getUniqueId(), label));

        SendCommand.Result commandSendResult = future.join();
        return (commandSendResult instanceof SendCommand.Success);
    }

    /**
     * Экспортировать удаление активной игры из
     * сети единого сервера Bridgenet.
     *
     * @param description - описание подключенной и активной игры.
     */
    public void exportGameDelete(ActiveGameDescription description) {
        channel.sendMessage(new DeleteGame(description.getGameId(), description.getActiveId()));
    }

    /**
     * Отправить сообщение об отсоединении текущего устройства
     */
    public void exportDeviceDisconnect() {
        if (currentDeviceId != null) {
            channel.sendMessage(new Disconnect(currentDeviceId, Disconnect.Type.SERVER));
        }
    }

    /**
     * Отправить сообщение об отсоединении пользователя.
     * @param description - описание подключаемого пользователя.
     */
    public void exportUserDisconnect(UserDescription description) {
        channel.sendMessage(new Disconnect(description.getUniqueId(), Disconnect.Type.PLAYER));
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
        CompletableFuture<Handshake.Result> future = channel.sendMessageWithResponse(Handshake.Result.class,
                new Handshake(Handshake.Type.PLAYER,
                        prepareUserHandshakeProperties(description)));

        Handshake.Result result = future.join();

        boolean equals = result.getKey().equals(description.getUniqueId());
        return (result instanceof Handshake.Success) && equals;
    }

    /**
     * Экспортировать отправку текстового сообщения пользователю
     * через сеть единого сервера Bridgenet.
     *
     * @param chatType - тип чата, в котором отрисовать сообщение пользователю
     * @param playerId - идентификатор активного пользователя.
     * @param message - текстовое сообщение
     */
    public void exportUserMessageSend(SendMessage.ChatType chatType, UUID playerId, String message) {
        channel.sendMessage(new SendMessage(playerId, message, chatType));
    }

    /**
     * Экспортировать процесс переподключения игрока на
     * другой подключенный сервер на сеть единого сервера Bridgenet.
     *
     * @param playerId - идентификатор переподключаемого игрока.
     * @param serverId - идентификатор устройства, на который переподключать.
     *
     * @return - возвращает TRUE в случае удачной попытки переподключения.
     */
    public boolean exportUserRedirectWithResult(UUID playerId, UUID serverId) {
        CompletableFuture<Redirect.Result> future = channel.sendMessageWithResponse(Redirect.Result.class,
                new Redirect(playerId, serverId));

        Redirect.Result result = future.join();
        return (result instanceof Handshake.Success);
    }

    /**
     * Экспортировать процесс переподключения игрока на
     * другой подключенный сервер на сеть единого сервера Bridgenet.
     *
     * @param playerId - идентификатор переподключаемого игрока.
     * @param serverId - идентификатор устройства, на который переподключать.
     */
    public void exportUserRedirect(UUID playerId, UUID serverId) {
        channel.sendMessage(new Redirect(playerId, serverId));
    }

    /**
     * Экспортировать процесс переподключения игрока на
     * текущий подключенный сервер на сеть единого сервера Bridgenet.
     *
     * @param playerId - идентификатор переподключаемого игрока.
     *
     * @return - возвращает TRUE в случае удачной попытки переподключения.
     */
    public boolean exportUserRedirectToHereWithResult(UUID playerId) {
        if (currentDeviceId != null) {
            return exportUserRedirectWithResult(playerId, currentDeviceId);
        }
        return false;
    }

    /**
     * Экспортировать процесс переподключения игрока на
     * текущий подключенный сервер на сеть единого сервера Bridgenet.
     *
     * @param playerId - идентификатор переподключаемого игрока.
     */
    public void exportUserRedirectToHere(UUID playerId) {
        if (currentDeviceId != null) {
            exportUserRedirect(playerId, currentDeviceId);
        }
    }

    /**
     * Экспортировать отправку текстового сообщения пользователю
     * на весь экран через сеть единого сервера Bridgenet.
     *
     * @param playerId - идентификатор активного пользователя.
     * @param description - описание дополнительных параметров сообщения.
     */
    public void exportUserTitleSend(UUID playerId, TitleDescription description) {
        channel.sendMessage(new SendTitle(playerId,
                description.getTitle(),
                description.getSubtitle(),
                description.getFadeIn(),
                description.getStay(),
                description.getFadeOut()));
    }

    /**
     * Запросить у единого сервера Bridgenet список команд,
     * зарегистрированных в нем для возможности исполнения на данном устройстве.
     */
    public List<String> lookupServerCommandsList() {
        CompletableFuture<GetCommands.Result> future
                = channel.sendMessageWithResponse(GetCommands.Result.class, new GetCommands());

        GetCommands.Result result = future.join();
        return result.getList();
    }
}
