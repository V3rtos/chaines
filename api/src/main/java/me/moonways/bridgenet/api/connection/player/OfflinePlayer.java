package me.moonways.bridgenet.api.connection.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.connection.server.type.SpigotServer;
import me.moonways.bridgenet.api.connection.server.type.VelocityServer;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class OfflinePlayer implements Player {

    private final int playerId;

    private final String name;

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return false;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        throw new UnsupportedOperationException("offline");
    }

    @Override
    public void performCommand(@NotNull String command) {
        throw new UnsupportedOperationException("offline");
    }

    @Override
    public VelocityServer getVelocityServer() {
        throw new UnsupportedOperationException("offline");
    }

    @Override
    public SpigotServer getSpigotServer() {
        throw new UnsupportedOperationException("offline");
    }

    @Override
    public void redirect(@NotNull SpigotServer server) {
        throw new UnsupportedOperationException("offline");
    }
}
