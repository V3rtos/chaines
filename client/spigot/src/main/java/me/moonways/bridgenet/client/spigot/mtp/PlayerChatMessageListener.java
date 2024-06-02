package me.moonways.bridgenet.client.spigot.mtp;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.message.SendMessage;
import me.moonways.bridgenet.model.message.SendTitle;
import me.moonways.bridgenet.mtp.message.persistence.InboundMessageListener;
import me.moonways.bridgenet.mtp.message.persistence.SubscribeMessage;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Server;
import org.bukkit.entity.Player;

@InboundMessageListener
public class PlayerChatMessageListener {

    @Inject
    private Server server;

    @SubscribeMessage
    public void handle(SendMessage message) {
        Player player = server.getPlayer(message.getPlayerId());

        if (player == null) {
            return;
        }

        String text = message.getMessage();
        switch (message.getChatType()) {
            case CHAT: {
                player.sendMessage(text);
                break;
            }
            case ACTION_BAR: {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));
                break;
            }
        }
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
