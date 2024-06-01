package me.moonways.bridgenet.client.api.minecraft.server;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.client.api.BridgenetServerSync;
import me.moonways.bridgenet.client.api.data.UserDto;

import java.util.List;
import java.util.UUID;

@Autobind
public class MinecraftCommandsEngine {

    private List<String> bridgenetCommandAliases;
    private BridgenetServerSync bridgenet;

    /**
     * Проинициализировать исполнитель команд Bridgenet
     * относительно Bukkit-игроков.
     *
     * @param connector - соединитель в оболочке под спигот сервер.
     */
    public void init(MinecraftServerConnector connector) {
        bridgenet = connector.getBridgenetServerSync();

        bridgenetCommandAliases = bridgenet.lookupServerCommandsList();
        bridgenetCommandAliases.replaceAll(String::toLowerCase);
    }

    /**
     * Возможно ли исполнить данную команду на стороне
     * единого сервера Bridgenet.
     *
     * @param label - введенная игроком строка команды.
     */
    public boolean isExportable(String label) {
        String commandName = label.replaceFirst("/", "").split(" ")[0];
        return bridgenetCommandAliases.contains(commandName.toLowerCase());
    }

    /**
     * Экспортировать исполнение команды на единый
     * сервер Bridgenet и делегировать данную ответственность
     * на него.
     *
     * @param playerId - идентификатор игрока, который исполняет команду
     * @param label  - введенная игроком строка команды.
     * @return - возвращает TRUE если команда была успешно исполнена.
     */
    public boolean exportCommand(UUID playerId, String label) {
        if (!isExportable(label)) {
            throw new IllegalArgumentException(label);
        }

        UserDto user = UserDto.builder()
                .uniqueId(playerId)
                .build();

        return bridgenet.exportCommandSend(user, label);
    }
}
