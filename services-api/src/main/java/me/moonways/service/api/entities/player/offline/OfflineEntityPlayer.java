package me.moonways.service.api.entities.player.offline;

import me.moonways.service.api.entities.server.EntityServer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.service.api.entities.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class OfflineEntityPlayer implements EntityPlayer, Serializable {

    private static final long serialVersionUID = 4063154190690621348L;

    private final UUID uniqueId;

    private final String name;

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return false; // todo
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
    public EntityServer getVelocityServer() {
        throw new UnsupportedOperationException("offline");
    }

    @Override
    public EntityServer getSpigotServer() {
        throw new UnsupportedOperationException("offline");
    }

    @Override
    public void redirect(@NotNull EntityServer server) {
        throw new UnsupportedOperationException("offline");
    }
}
