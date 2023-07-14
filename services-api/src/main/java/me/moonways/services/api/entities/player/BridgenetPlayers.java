package me.moonways.services.api.entities.player;

import me.moonways.services.api.entities.player.offline.OfflineEntityPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface BridgenetPlayers {

    OfflineEntityPlayer getOfflinePlayer(@NotNull UUID playerUUID);

    OfflineEntityPlayer getOfflinePlayer(@NotNull String playerName);

    void addConnectedPlayer(@NotNull ConnectedEntityPlayer connectedPlayer);

    void removeConnectedPlayer(@NotNull ConnectedEntityPlayer connectedPlayer);

    String findPlayerName(@NotNull UUID playerUUID);

    UUID findPlayerId(@NotNull String playerName);

    ConnectedEntityPlayer getConnectedPlayer(@NotNull UUID playerUUID);

    ConnectedEntityPlayer getConnectedPlayer(@NotNull String name);
}
