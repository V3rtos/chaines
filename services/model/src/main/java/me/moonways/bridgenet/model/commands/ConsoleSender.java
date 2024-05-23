package me.moonways.bridgenet.model.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.model.language.Message;
import me.moonways.bridgenet.model.permissions.permission.Permission;
import me.moonways.bridgenet.model.util.audience.ComponentHolders;
import me.moonways.bridgenet.model.util.audience.EntityAudience;
import me.moonways.bridgenet.model.util.audience.event.AudienceSendEvent;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.rmi.RemoteException;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public class ConsoleSender implements EntityAudience {

    private final String name;

    public static ConsoleSender BRIDGENET_CONSOLE = new ConsoleSender("Bridgenet console:");

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

    @Override
    public boolean hasPermission(@NotNull Permission permission) throws RemoteException {
        return false;
    }

    @Override
    public boolean hasPermission(@NotNull String permissionName) throws RemoteException {
        return false;
    }
}
