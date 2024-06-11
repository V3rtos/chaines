package me.moonways.bridgenet.client.spigot.service.gui;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.client.spigot.BridgenetSpigotGuiEngine;
import me.moonways.bridgenet.client.spigot.BridgenetSpigotPlugin;
import me.moonways.bridgenet.model.message.CloseGui;
import me.moonways.bridgenet.model.message.OpenGui;
import me.moonways.bridgenet.model.service.gui.GuiSlot;
import me.moonways.bridgenet.mtp.message.persistence.InboundMessageListener;
import me.moonways.bridgenet.mtp.message.persistence.SubscribeMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@InboundMessageListener
public class BridgenetGuiListener {

    @Inject
    private BridgenetSpigotGuiEngine spigotGuiEngine;
    @Inject
    private RemoteItemParser remoteItemParser;
    @Inject
    private BridgenetSpigotPlugin plugin;

    @SubscribeMessage
    public void handle(OpenGui openGui) {
        Player player = Bukkit.getPlayer(openGui.getPlayerId());

        if (player != null) {

            plugin.getServer().getScheduler().runTask(plugin, () -> {

                BukkitGui bukkitGui = new BukkitGui(openGui.getGuiId(), openGui.getDescription());
                bukkitGui.initInventory(player);

                for (OpenGui.GuiItemView item : openGui.getItems()) {
                    ItemStack itemStack = remoteItemParser.normalize(item.getItemStack());

                    bukkitGui.set(GuiSlot.at(item.getSlot()), itemStack);
                }

                spigotGuiEngine.save(player, bukkitGui);
                bukkitGui.open(player);
            });
        }
    }

    @SubscribeMessage
    public void handle(CloseGui closeGui) {
        Player player = Bukkit.getPlayer(closeGui.getPlayerId());

        if (player != null) {

            plugin.getServer().getScheduler().runTask(plugin, () -> {

                spigotGuiEngine.invalidate(player);
                player.closeInventory();
            });
        }
    }
}
