package me.moonways.bridgenet.client.spigot;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.client.spigot.service.gui.BukkitGui;
import me.moonways.bridgenet.client.spigot.service.gui.RemoteItemParser;
import me.moonways.bridgenet.model.service.gui.GuiServiceModel;
import me.moonways.bridgenet.model.service.gui.GuiSlot;
import me.moonways.bridgenet.model.service.gui.click.ClickAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.rmi.RemoteException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Класс BridgenetSpigotGuiEngine отвечает за управление графическими интерфейсами (GUI) на стороне сервера Spigot
 * и обработку взаимодействий с ними.
 */
@Autobind
public final class BridgenetSpigotGuiEngine {

    private final Cache<UUID, BukkitGui> remoteGuisCache =
            CacheBuilder.newBuilder()
                    .expireAfterAccess(10, TimeUnit.MINUTES)
                    .build();

    @Inject
    private RemoteItemParser remoteItemParser;
    @Inject
    private BeansService beansService;

    @Inject
    private GuiServiceModel guiServiceModel;

    // todo - добавить автоматическое закрытие гуи после его инвалидности в кеше.

    /**
     * Обрабатывает действие клика по GUI.
     *
     * @param event событие клика в инвентаре.
     */
    public void sendClickAction(InventoryClickEvent event) {
        UUID playerId = event.getWhoClicked().getUniqueId();

        remoteGuisCache.cleanUp();
        BukkitGui bukkitGui = remoteGuisCache.getIfPresent(playerId);

        if (bukkitGui == null) {
            return;
        }

        try {
            event.setCancelled(true);
            guiServiceModel.fireClickAction(
                    ClickAction.builder()
                            .playerId(playerId)
                            .guiId(bukkitGui.getId())
                            .slot(GuiSlot.at(event.getSlot() + 1))
                            .clickType(remoteItemParser.remote(event.getClick()))
                            .build());
        } catch (RemoteException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Сохраняет GUI для указанного игрока в кэш.
     *
     * @param player игрок, для которого сохраняется GUI.
     * @param bukkitGui GUI, который необходимо сохранить.
     */
    public void save(Player player, BukkitGui bukkitGui) {
        beansService.inject(bukkitGui);

        remoteGuisCache.cleanUp();
        remoteGuisCache.put(player.getUniqueId(), bukkitGui);
    }

    /**
     * Удаляет сохраненный GUI для указанного игрока из кэша.
     *
     * @param player игрок, для которого необходимо удалить GUI.
     */
    public void invalidate(Player player) {
        remoteGuisCache.invalidate(player.getUniqueId());
    }
}
