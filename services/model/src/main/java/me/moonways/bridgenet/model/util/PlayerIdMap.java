package me.moonways.bridgenet.model.util;

import lombok.SneakyThrows;
import me.moonways.bridgenet.model.players.Player;
import me.moonways.bridgenet.model.players.service.PlayerStore;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class PlayerIdMap<V> extends ConcurrentHashMap<UUID, V> {

    private static final long serialVersionUID = 8540228284873559054L;

    private final PlayerStore playerStore;

    public PlayerIdMap(PlayerStore playerStore) {
        super();
        this.playerStore = playerStore;
    }

    public PlayerIdMap(PlayerStore playerStore, PlayerIdMap<V> other) {
        super(other);
        this.playerStore = playerStore;
    }

    @SneakyThrows
    private void checkIfNotOnline() {
        for (UUID playerId : new HashSet<>(keySet())) {

            if (!playerStore.get(playerId).isPresent()) {
                remove(playerId);
            }
        }
    }

    public synchronized Optional<V> get(UUID playerId) {
        checkIfNotOnline();
        return Optional.ofNullable(super.get(playerId));
    }

    public synchronized Optional<V> get(Player player) throws RemoteException {
        return get(player.getId());
    }

    public synchronized V getOrDefault(UUID playerId, Supplier<V> def) {
        return get(playerId).orElseGet(def);
    }

    public synchronized V getOrDefault(Player player, Supplier<V> def) throws RemoteException {
        return getOrDefault(player.getId(), def);
    }

    public synchronized V getOrPut(UUID playerId, Supplier<V> factory) {
        Optional<V> optional = get(playerId);

        if (!optional.isPresent()) {
            V value = factory.get();
            put(playerId, value);

            return value;
        }

        return optional.get();
    }

    public synchronized V getOrPut(Player player, Supplier<V> factory) throws RemoteException {
        return getOrPut(player.getId(), factory);
    }

    public synchronized V remove(Player player) throws RemoteException {
        return remove(player.getId());
    }
}
