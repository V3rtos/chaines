package me.moonways.bridgenet.model.players.connection;

import me.moonways.bridgenet.model.bus.message.SendChatMessage;
import me.moonways.bridgenet.model.bus.message.SendTitleMessage;
import me.moonways.bridgenet.model.players.EntityPlayer;
import me.moonways.bridgenet.model.players.Title;
import me.moonways.bridgenet.model.servers.EntityServer;
import me.moonways.bridgenet.model.players.offline.OfflineEntityPlayer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.moonways.bridgenet.mtp.MTPMessageSender;
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
        try {
            MTPMessageSender channel = spigotServer.getChannel();
            channel.sendMessage(
                    new SendChatMessage(getUniqueId(), message, SendChatMessage.ChatType.CHAT));
        }
        catch (RemoteException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public CompletableFuture<Boolean> redirect(@NotNull EntityServer server) {
        CompletableFuture<Boolean> connectFuture;
        try {
            connectFuture = server.connectThat(this);
        }
        catch (RemoteException exception) {
            connectFuture = CompletableFuture.completedFuture(false);
            connectFuture.completeExceptionally(exception);
        }

        return connectFuture;
    }

    @Override
    public void sendTitle(@Nullable String title, @Nullable String subtitle) {
        sendTitle(Title.builder()
                .fadeIn(0).stay(3).fadeOut(2)
                .title(title)
                .subtitle(subtitle)
                .build());
    }

    @Override
    public void sendTitle(@NotNull Title title) {
        try {
            MTPMessageSender channel = spigotServer.getChannel();
            channel.sendMessage(
                    new SendTitleMessage(getUniqueId(), title.getTitle(), title.getSubtitle(),
                            title.getFadeIn(), title.getStay(), title.getFadeOut()));
        }
        catch (RemoteException exception) {
            throw new RuntimeException(exception);
        }
    }
}
