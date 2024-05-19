package me.moonways.endpoint.players;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.model.bus.HandshakePropertiesConst;
import me.moonways.bridgenet.model.players.OfflinePlayer;
import me.moonways.bridgenet.model.players.Player;
import me.moonways.bridgenet.model.players.service.PlayerStore;
import me.moonways.bridgenet.model.util.PlayerIdMap;
import me.moonways.endpoint.players.database.EntityNamespace;
import me.moonways.endpoint.players.database.EntityPlayer;
import me.moonways.endpoint.players.database.PlayerDescription;
import me.moonways.endpoint.players.database.PlayersRepository;
import me.moonways.endpoint.players.player.OfflinePlayerStub;
import me.moonways.endpoint.players.player.PlayerStub;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Log4j2
@RequiredArgsConstructor
public final class PlayerStoreStub implements PlayerStore {

    private final Cache<UUID, PlayerDescription> nameByIdsCache = CacheBuilder.newBuilder()
            .expireAfterAccess(3, TimeUnit.HOURS)
            .build();
    private final Cache<String, PlayerDescription> idByNamesCache = CacheBuilder.newBuilder()
            .expireAfterAccess(3, TimeUnit.HOURS)
            .build();

    private final Cache<UUID, OfflinePlayer> offlinePlayersCache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();

    @Getter
    private final PlayerIdMap<Player> onlinePlayersMap = new PlayerIdMap<>(this);

    @Inject
    private PlayersRepository playersRepository;
    @Inject
    private BeansService beansService;

    public Player addOnlinePlayer(Properties properties) throws RemoteException {
        Player player = createPlayer(properties);

        beansService.inject(player);
        onlinePlayersMap.put(player.getId(), player);

        return player;
    }

    public Player removeOnlinePlayer(UUID id) throws RemoteException {
        Player removed = onlinePlayersMap.remove(id);

        nameByIdsCache.cleanUp();
        idByNamesCache.cleanUp();
        offlinePlayersCache.cleanUp();

        return removed;
    }

    private Player createPlayer(Properties properties) throws RemoteException {
        UUID uuid = UUID.fromString(properties.getProperty(HandshakePropertiesConst.USER_UUID));
        String username = properties.getProperty(HandshakePropertiesConst.USER_NAME);

        UUID proxyId = UUID.fromString(properties.getProperty(HandshakePropertiesConst.USER_PROXY_ID));

        PlayerDescription description;
        if (idByName(username) == null) {
            EntityNamespace entityNamespace = EntityNamespace.builder()
                    .uuid(uuid)
                    .name(username.toLowerCase())
                    .build();
            playersRepository.insert(
                    EntityPlayer.builder()
                            .namespace(entityNamespace)
                            .description(description = PlayerDescription.builder()
                                    .lastLoggedProxyId(proxyId)
                                    .lastLoggedTime(System.currentTimeMillis())
                                    .totalExperience(0)
                                    .namespace(entityNamespace)
                                    .build())
                            .build());
        } else {
            description = idByNamesCache.getIfPresent(username.toLowerCase());
        }
        return new PlayerStub(uuid, username, description, this);
    }

    private OfflinePlayer createOfflinePlayer(UUID id) throws RemoteException {
        String name = nameById(id);
        if (name == null) {
            return null;
        }

        PlayerDescription description = nameByIdsCache.getIfPresent(id);
        if (description == null) {
            return null;
        }

        EntityNamespace namespace = description.getNamespace();
        OfflinePlayer offlinePlayer = new OfflinePlayerStub(namespace.getUuid(), namespace.getName(), description);

        beansService.inject(offlinePlayer);

        return offlinePlayer;
    }

    @Override
    public OfflinePlayer getOffline(UUID id) throws RemoteException {
        if (id == null) {
            return null;
        }

        Optional<OfflinePlayer> offlinePlayerOptional = get(id).map(player -> player);
        if (offlinePlayerOptional.isPresent()) {
            return offlinePlayerOptional.get();
        }

        offlinePlayersCache.cleanUp();

        OfflinePlayer offlinePlayer = offlinePlayersCache.getIfPresent(id);
        if (offlinePlayer == null) {
            offlinePlayer = createOfflinePlayer(id);

            if (offlinePlayer != null) {
                offlinePlayersCache.put(id, offlinePlayer);
            }
        }

        return offlinePlayer;
    }

    @Override
    public OfflinePlayer getOffline(String name) throws RemoteException {
        if (name == null) {
            return null;
        }
        return getOffline(idByName(name));
    }

    @Override
    public Optional<Player> get(UUID id) throws RemoteException {
        if (id == null) {
            return Optional.empty();
        }
        return onlinePlayersMap.get(id);
    }

    @Override
    public Optional<Player> get(String name) throws RemoteException {
        if (name == null) {
            return Optional.empty();
        }
        return get(idByName(name));
    }

    @Override
    public String nameById(UUID id) throws RemoteException {
        nameByIdsCache.cleanUp();

        PlayerDescription cachedDescription = nameByIdsCache.getIfPresent(id);
        if (cachedDescription == null) {
            playersRepository.get(id)
                    .map(EntityPlayer::getDescription)
                    .ifPresent(playerDescription -> nameByIdsCache.put(id, playerDescription));
        }

        return Optional.ofNullable(cachedDescription)
                .map(PlayerDescription::getNamespace)
                .map(EntityNamespace::getName)
                .orElse(null);
    }

    @Override
    public UUID idByName(String name) throws RemoteException {
        idByNamesCache.cleanUp();

        PlayerDescription cachedDescription = idByNamesCache.getIfPresent(name.toLowerCase());
        if (cachedDescription == null) {
            playersRepository.get(name)
                    .map(EntityPlayer::getDescription)
                    .ifPresent(playerDescription -> idByNamesCache.put(name.toLowerCase(), playerDescription));
        }

        return Optional.ofNullable(cachedDescription)
                .map(PlayerDescription::getNamespace)
                .map(EntityNamespace::getUuid)
                .orElse(null);
    }
}
