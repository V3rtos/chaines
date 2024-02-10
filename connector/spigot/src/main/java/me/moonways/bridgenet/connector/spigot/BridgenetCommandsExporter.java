package me.moonways.bridgenet.connector.spigot;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.connector.BridgenetServerSync;
import me.moonways.bridgenet.connector.description.UserDescription;
import me.moonways.bridgenet.model.bus.message.SendCommand;
import me.moonways.bridgenet.mtp.MTPMessageSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Autobind
public class BridgenetCommandsExporter {

    private List<String> bridgenetCommandAliases;
    private BridgenetServerSync bridgenet;

    /**
     * Проинициализировать исполнитель команд Bridgenet
     * относительно Bukkit-игроков.
     *
     * @param connector - соединитель в оболочке под спигот сервер.
     */
    public void init(BridgenetSpigotConnector connector) {
        bridgenet = connector.getBridgenetServerSync();

        bridgenetCommandAliases = bridgenet.lookupBridgenetServerCommandsList();
        bridgenetCommandAliases.replaceAll(String::toLowerCase);
    }

    /**
     * Возможно ли исполнить данную команду на стороне
     * единого сервера Bridgenet.
     *
     * @param label - введенная игроком строка команды.
     */
    public boolean isExportable(String label) {
        String commandName = label.replaceFirst("\\/", "").split(" ")[0];
        return bridgenetCommandAliases.contains(commandName.toLowerCase());
    }

    /**
     * Экспортировать исполнение команды на единый
     * сервер Bridgenet и делегировать данную ответственность
     * на него.
     *
     * @param player - игрок, который исполняет команду
     * @param label - введенная игроком строка команды.
     *
     * @return - возвращает TRUE если команда была успешно исполнена.
     */
    public boolean exportCommand(Player player, String label) {
        if (!isExportable(label)) {
            throw new IllegalArgumentException(label);
        }

        UserDescription userDescription = UserDescription.builder()
                .uniqueId(player.getUniqueId())
                .build();

        return bridgenet.exportSendCommand(userDescription, label);
    }
}
