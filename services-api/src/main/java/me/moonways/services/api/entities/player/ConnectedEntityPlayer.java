package me.moonways.services.api.entities.player;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.moonways.services.api.entities.player.offline.OfflineEntityPlayer;
import me.moonways.services.api.entities.server.EntityServer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public class ConnectedEntityPlayer extends OfflineEntityPlayer implements EntityPlayer {

    private final EntityServer velocityServer;

    @Setter(AccessLevel.PRIVATE)
    private EntityServer spigotServer;

    public ConnectedEntityPlayer(UUID uuid, String name, EntityServer velocityServer, EntityServer spigotServer) {
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
    public void redirect(@NotNull EntityServer server) {
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
