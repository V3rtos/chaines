package me.moonways.model.players;

import me.moonways.model.players.offline.OfflineEntityPlayer;
import me.moonways.model.servers.EntityServer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public class ConnectedEntityPlayer extends OfflineEntityPlayer implements EntityPlayer, Serializable {

    private static final long serialVersionUID = 2748610226487304099L;

    private final EntityServer velocityServer;

    @Setter(AccessLevel.PRIVATE)
    private EntityServer spigotServer;

    public ConnectedEntityPlayer(UUID uuid, String name, EntityServer velocityServer, EntityServer spigotServer) {
        super(uuid, name);
        this.velocityServer = velocityServer;
        this.spigotServer = spigotServer;
    }

    @Override
    public void sendMessage(@Nullable String message) {
        // todo
    }

    @Override
    public void redirect(@NotNull EntityServer server) {
        CompletableFuture<Boolean> connectFuture;

        try {
            connectFuture = server.connect(this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

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
