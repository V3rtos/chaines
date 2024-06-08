package me.moonways.bridgenet.client.spigot;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.client.api.BridgenetServerSync;
import me.moonways.bridgenet.client.api.data.UserDto;
import me.moonways.bridgenet.model.message.SendMessage;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
public final class BridgenetSpigotPlayersEngine {
    private final BridgenetServerSync bridgenet;

    public void firePlayerJoin(Player player) {
        bridgenet.exportUserHandshake(
                UserDto.builder()
                        .uniqueId(player.getUniqueId())
                        .name(player.getName())
                        .proxyId(UUID.randomUUID())
                        .build());

        firePlayerRedirectToThis(player);
    }

    public void firePlayerQuit(Player player) {
        bridgenet.exportUserDisconnect(
                UserDto.builder()
                        .uniqueId(player.getUniqueId())
                        .build());
    }

    public void firePlayerRedirectToThis(Player player) {
        bridgenet.exportUserRedirect(
                player.getUniqueId(),
                bridgenet.getCurrentClientId());
    }

    public void handleSendMessage(SendMessage sendMessage) {
        Player player = Bukkit.getPlayer(sendMessage.getPlayerId());

        if (player == null) {
            return;
        }

        ChatMessageType chatMessageType = getSpigotChatType(sendMessage.getChatType());
        BaseComponent[] components = TextComponent.fromLegacyText(sendMessage.getMessage().replace("\\\\n", "\n"));

        player.spigot().sendMessage(chatMessageType, components);
    }

    private ChatMessageType getSpigotChatType(SendMessage.ChatType chatType) {
        switch (chatType) {
            case CHAT:
                return ChatMessageType.CHAT;
            case ACTION_BAR:
                return ChatMessageType.ACTION_BAR;
        }
        return ChatMessageType.SYSTEM;
    }
}
