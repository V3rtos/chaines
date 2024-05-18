package me.moonways.endpoint.players.player;

import lombok.Getter;
import me.moonways.bridgenet.model.language.Message;
import me.moonways.bridgenet.model.players.Player;
import me.moonways.bridgenet.model.players.service.PlayerConnection;
import me.moonways.bridgenet.model.players.service.statistic.ActivityStatistics;
import me.moonways.bridgenet.model.util.TitleFade;
import me.moonways.bridgenet.model.util.audience.ComponentHolders;
import me.moonways.bridgenet.model.util.audience.event.AudienceSendEvent;
import me.moonways.endpoint.players.database.PlayerDescription;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public class PlayerStub extends OfflinePlayerStub implements Player {

    private final ActivityStatistics statistics = new ActivityStatisticsStub();
    private final PlayerConnection connection;

    public PlayerStub(UUID id, String name, PlayerDescription description) {
        super(id, name, description);
        this.connection = new PlayerConnectionStub(this);
    }

    @Override
    public boolean isOnline() throws RemoteException {
        return true;
    }

    @Override
    public CompletableFuture<AudienceSendEvent> sendActionbar(Component message) throws RemoteException {
        return null;
    }

    @Override
    public CompletableFuture<AudienceSendEvent> sendActionbar(Message message) throws RemoteException {
        return null;
    }

    @Override
    public CompletableFuture<AudienceSendEvent> sendActionbar(String message) throws RemoteException {
        return null;
    }

    @Override
    public CompletableFuture<AudienceSendEvent> sendTitle(Component title, Component subtitle, TitleFade fade) throws RemoteException {
        return null;
    }

    @Override
    public CompletableFuture<AudienceSendEvent> sendTitle(Component title, Component subtitle) throws RemoteException {
        return null;
    }

    @Override
    public CompletableFuture<AudienceSendEvent> sendTitle(Component title) throws RemoteException {
        return null;
    }

    @Override
    public CompletableFuture<AudienceSendEvent> sendTitle(Message title, Message subtitle, TitleFade fade) throws RemoteException {
        return null;
    }

    @Override
    public CompletableFuture<AudienceSendEvent> sendTitle(Message title, Message subtitle) throws RemoteException {
        return null;
    }

    @Override
    public CompletableFuture<AudienceSendEvent> sendTitle(Message title) throws RemoteException {
        return null;
    }

    @Override
    public CompletableFuture<AudienceSendEvent> sendTitle(String title, String subtitle, TitleFade fade) throws RemoteException {
        return null;
    }

    @Override
    public CompletableFuture<AudienceSendEvent> sendTitle(String title, String subtitle) throws RemoteException {
        return null;
    }

    @Override
    public CompletableFuture<AudienceSendEvent> sendTitle(String title) throws RemoteException {
        return null;
    }

    @Override
    public Optional<AudienceSendEvent> sendMessage(@NotNull Component message) throws RemoteException {
        return Optional.empty();
    }

    @Override
    public Optional<AudienceSendEvent> sendMessage(@NotNull Message message) throws RemoteException {
        return Optional.empty();
    }

    @Override
    public Optional<AudienceSendEvent> sendMessage(@Nullable String message) throws RemoteException {
        return Optional.empty();
    }

    @Override
    public Optional<AudienceSendEvent> sendMessage(@NotNull Component message, @NotNull ComponentHolders holders) throws RemoteException {
        return Optional.empty();
    }

    @Override
    public Optional<AudienceSendEvent> sendMessage(@NotNull Message message, @NotNull ComponentHolders holders) throws RemoteException {
        return Optional.empty();
    }

    @Override
    public Optional<AudienceSendEvent> sendMessage(@Nullable String message, @NotNull ComponentHolders holders) throws RemoteException {
        return Optional.empty();
    }
}
