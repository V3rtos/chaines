package me.moonways.model.players.offline;

import me.moonways.model.servers.EntityServer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.model.players.EntityPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
    public void sendMessage(@Nullable String message) {
        throw new UnsupportedOperationException("offline");
    }

    @Override
    public void sendMessage(@NotNull String message, @Nullable Object... replacements) {
        sendMessage(String.format(message, replacements));
    }

    @Override
    public EntityServer getVelocityServer() {
        return null;
    }

    @Override
    public EntityServer getSpigotServer() {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> redirect(@NotNull EntityServer server) {
        CompletableFuture<Boolean> completableFuture = CompletableFuture.completedFuture(false);
        completableFuture.completeExceptionally(new UnsupportedOperationException("offline"));

        return completableFuture;
    }
}
