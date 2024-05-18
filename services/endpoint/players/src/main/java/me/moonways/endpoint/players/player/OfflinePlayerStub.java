package me.moonways.endpoint.players.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.language.Message;
import me.moonways.bridgenet.model.permissions.PermissionsServiceModel;
import me.moonways.bridgenet.model.permissions.group.PermissionGroup;
import me.moonways.bridgenet.model.permissions.permission.Permission;
import me.moonways.bridgenet.model.players.OfflinePlayer;
import me.moonways.bridgenet.model.util.audience.ComponentHolders;
import me.moonways.bridgenet.model.util.audience.event.AudienceSendEvent;
import me.moonways.endpoint.players.PlayerLevelingStub;
import me.moonways.endpoint.players.database.PlayerDescription;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class OfflinePlayerStub implements OfflinePlayer {

    private final UUID id;
    private final String name;

    private final PlayerDescription description;

    @Inject
    private PermissionsServiceModel permissionsServiceModel;

    @Override
    public Set<Permission> getPermissions() throws RemoteException {
        return permissionsServiceModel.getPermissions().getActivePermissions(id);
    }

    @Override
    public PermissionGroup getGroup() throws RemoteException {
        return permissionsServiceModel.getGroups().getPlayerGroup(id).get();
    }

    @Override
    public int getLevel() throws RemoteException {
        return PlayerLevelingStub.getLevel(getTotalExperience());
    }

    @Override
    public int getTotalExperience() throws RemoteException {
        return description.getTotalExperience();
    }

    @Override
    public int getExperience() throws RemoteException {
        return getTotalExperience() - getExperienceToNextLevel();
    }

    @Override
    public int getExperienceToNextLevel() throws RemoteException {
        return PlayerLevelingStub.getExperienceToNextLevel(getLevel());
    }

    @Override
    public boolean isOnline() throws RemoteException {
        return false;
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

    @Override
    public boolean hasPermission(@NotNull Permission permission) throws RemoteException {
        return permissionsServiceModel.getPermissions().hasPermission(id, permission);
    }

    @Override
    public boolean hasPermission(@NotNull String permissionName) throws RemoteException {
        return permissionsServiceModel.getPermissions().hasPermission(id, permissionName);
    }
}
