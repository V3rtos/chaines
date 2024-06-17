package me.moonways.bridgenet.client.spigot.service.player;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.client.spigot.BridgenetSpigotPlayersEngine;
import me.moonways.bridgenet.model.message.SendMessage;
import me.moonways.bridgenet.model.message.SendTitle;
import me.moonways.bridgenet.mtp.message.persistence.InboundMessageListener;
import me.moonways.bridgenet.mtp.message.persistence.SubscribeMessage;
import org.bukkit.Server;
import org.bukkit.entity.Player;

@InboundMessageListener
public class BridgenetPlayerChatListener {

    @Inject
    private BridgenetSpigotPlayersEngine spigotPlayersEngine;
    @Inject
    private Server server;

    @SubscribeMessage
    public void handle(SendMessage message) {
        spigotPlayersEngine.handleSendMessage(message);
    }

    @SubscribeMessage
    public void handle(SendTitle message) {
        Player player = server.getPlayer(message.getPlayerId());

        if (player == null) {
            return;
        }

        player.sendTitle(
                message.getTitle(),
                message.getSubtitle(),
                message.getFadeIn(),
                message.getStay(),
                message.getFadeOut()
        );
    }
}
