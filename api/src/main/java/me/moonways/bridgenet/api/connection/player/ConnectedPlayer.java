package me.moonways.bridgenet.api.connection.player;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.moonways.bridgenet.api.connection.server.type.SpigotServer;
import me.moonways.bridgenet.api.connection.server.type.VelocityServer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@Getter
public class ConnectedPlayer extends OfflinePlayer implements Player {

    private final VelocityServer velocityServer;

    @Setter(AccessLevel.PRIVATE)
    private SpigotServer spigotServer;

    public ConnectedPlayer(UUID uuid, String name, VelocityServer velocityServer, SpigotServer spigotServer) {
        super(uuid, name);
        this.velocityServer = velocityServer;
        this.spigotServer = spigotServer;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        // todo
    }

    @Override
    public void performCommand(@NotNull String command) {
        // todo
    }

    @Override
    public void redirect(@NotNull SpigotServer server) {
        CompletableFuture<Boolean> connectFuture = server.connect(this);
        connectFuture.whenComplete((isSuccess, throwable) -> {

            if (isSuccess) {
                setSpigotServer(server);
            }
            else {
                throwable.printStackTrace();
            }
        });
    }
}
