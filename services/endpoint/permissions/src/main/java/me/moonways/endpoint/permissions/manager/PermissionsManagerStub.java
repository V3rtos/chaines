package me.moonways.endpoint.permissions.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.moonways.bridgenet.api.event.EventService;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.entity.EntityRepository;
import me.moonways.bridgenet.jdbc.entity.EntityRepositoryFactory;
import me.moonways.bridgenet.model.permissions.TemporalState;
import me.moonways.bridgenet.model.permissions.permission.Permission;
import me.moonways.bridgenet.model.permissions.permission.PermissionsManager;
import me.moonways.bridgenet.model.permissions.permission.PlayerPermissionPutEvent;
import me.moonways.bridgenet.model.permissions.permission.PlayerPermissionRemoveEvent;
import me.moonways.bridgenet.model.players.PlayersServiceModel;
import me.moonways.endpoint.permissions.entity.EntityPermission;

import java.rmi.RemoteException;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class PermissionsManagerStub implements PermissionsManager {

    private static Permission fromEntityPermission(EntityPermission entityPermission) {
        TemporalState temporalState = entityPermission.getExpirationTimeMillis() != null ?
                TemporalState.enabled(Duration.ofMillis(entityPermission.getExpirationTimeMillis() - System.currentTimeMillis())) :
                TemporalState.infinity();
        return new Permission(temporalState, entityPermission.getPermission());
    }

    @Inject
    private EventService eventService;
    @Inject
    private PlayersServiceModel playersServiceModel;
    @Inject
    private EntityRepositoryFactory repositoryFactory;

    private final Cache<UUID, Set<Permission>> playersPermissionsCache =
            CacheBuilder.newBuilder()
                    .expireAfterAccess(5, TimeUnit.HOURS)
                    .build();

    private Set<Permission> findPermissions(UUID playerId) {
        EntityRepository<EntityPermission> repository = repositoryFactory.fromEntityType(EntityPermission.class);
        return repository.searchManyIf(
                repository.newSearchMarker()
                        .withGet(EntityPermission::getPlayerId, playerId))
                .stream()
                .peek(entityPermission -> {
                    if (entityPermission.isExpired()) {
                        deletePermission(playerId, fromEntityPermission(entityPermission));
                    }
                })
                .filter(entityPermission -> !entityPermission.isExpired())
                .map(PermissionsManagerStub::fromEntityPermission)
                .collect(Collectors.toSet());
    }

    private void insertPermission(UUID playerId, Permission permission) {
        EntityRepository<EntityPermission> repository = repositoryFactory.fromEntityType(EntityPermission.class);
        repository.insert(EntityPermission.fromPermission(playerId, permission));
    }

    private void deletePermission(UUID playerId, Permission permission) {
        EntityRepository<EntityPermission> repository = repositoryFactory.fromEntityType(EntityPermission.class);
        repository.delete(EntityPermission.fromPermission(playerId, permission));
    }

    private void deleteAllPermissions(UUID playerId) {
        EntityRepository<EntityPermission> repository = repositoryFactory.fromEntityType(EntityPermission.class);
        repository.deleteIf(
                repository.newSearchMarker()
                        .withGet(EntityPermission::getPlayerId, playerId));
    }

    @Override
    public Set<Permission> getActivePermissions(String playerName) throws RemoteException {
        return getActivePermissions(playersServiceModel.store().idByName(playerName));
    }

    @Override
    public Set<Permission> getActivePermissions(UUID playerId) throws RemoteException {
        playersPermissionsCache.cleanUp();
        Set<Permission> cachedPermissionsSet = playersPermissionsCache.getIfPresent(playerId);

        if (cachedPermissionsSet == null) {
            cachedPermissionsSet = findPermissions(playerId);
            playersPermissionsCache.put(playerId, cachedPermissionsSet);
        }

        return cachedPermissionsSet;
    }

    @Override
    public Optional<PlayerPermissionPutEvent> addPermission(String playerName, Permission permission) throws RemoteException {
        return addPermission(playersServiceModel.store().idByName(playerName), permission);
    }

    @Override
    public Optional<PlayerPermissionPutEvent> addPermission(UUID playerId, Permission permission) throws RemoteException {
        if (hasPermission(playerId, permission)) {
            return Optional.empty();
        }

        getActivePermissions(playerId)
                .add(permission);

        insertPermission(playerId, permission);

        PlayerPermissionPutEvent event =
                PlayerPermissionPutEvent.builder()
                        .playerId(playerId)
                        .permission(permission)
                        .build();

        eventService.fireEvent(event);
        return Optional.of(event);
    }

    @Override
    public Optional<PlayerPermissionPutEvent> addPermission(String playerName, String permissionName) throws RemoteException {
        return addPermission(playerName, Permission.named(permissionName));
    }

    @Override
    public Optional<PlayerPermissionPutEvent> addPermission(UUID playerId, String permissionName) throws RemoteException {
        return addPermission(playerId, Permission.named(permissionName));
    }

    @Override
    public Optional<PlayerPermissionRemoveEvent> removePermission(String playerName, Permission permission) throws RemoteException {
        return removePermission(playersServiceModel.store().idByName(playerName), permission);
    }

    @Override
    public Optional<PlayerPermissionRemoveEvent> removePermission(UUID playerId, Permission permission) throws RemoteException {
        if (!hasPermission(playerId, permission)) {
            return Optional.empty();
        }

        getActivePermissions(playerId)
                .remove(permission);

        deletePermission(playerId, permission);

        PlayerPermissionRemoveEvent event =
                PlayerPermissionRemoveEvent.builder()
                        .playerId(playerId)
                        .permission(permission)
                        .build();

        eventService.fireEvent(event);
        return Optional.of(event);
    }

    @Override
    public Optional<PlayerPermissionRemoveEvent> removePermission(String playerName, String permissionName) throws RemoteException {
        return removePermission(playerName, Permission.named(permissionName));
    }

    @Override
    public Optional<PlayerPermissionRemoveEvent> removePermission(UUID playerId, String permissionName) throws RemoteException {
        return removePermission(playerId, Permission.named(permissionName));
    }

    @Override
    public void clearPermissions(String playerName) throws RemoteException {
        clearPermissions(playersServiceModel.store().idByName(playerName));
    }

    @Override
    public void clearPermissions(UUID playerId) throws RemoteException {
        playersPermissionsCache.invalidate(playerId);
        deleteAllPermissions(playerId);
    }

    @Override
    public boolean hasPermission(String playerName, Permission permission) throws RemoteException {
        return getActivePermissions(playerName).contains(permission);
    }

    @Override
    public boolean hasPermission(UUID playerId, Permission permission) throws RemoteException {
        return getActivePermissions(playerId).contains(permission);
    }

    @Override
    public boolean hasPermission(String playerName, String permissionName) throws RemoteException {
        return hasPermission(playerName, Permission.named(permissionName));
    }

    @Override
    public boolean hasPermission(UUID playerId, String permissionName) throws RemoteException {
        return hasPermission(playerId, Permission.named(permissionName));
    }
}
