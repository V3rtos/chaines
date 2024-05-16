package me.moonways.bridgenet.model.players.util;

import me.moonways.bridgenet.model.players.EntityPlayer;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class PlayerIdMap<V> extends ConcurrentHashMap<UUID, V> {

    private static final long serialVersionUID = 8540228284873559054L;

    public PlayerIdMap() {
        super();
    }

    public PlayerIdMap(PlayerIdMap<V> other) {
        super(other);
    }

    public synchronized Optional<V> get(UUID playerId) {
        return Optional.ofNullable(super.get(playerId));
    }

    public synchronized Optional<V> get(EntityPlayer player) throws RemoteException {
        return get(player.getUniqueId());
    }

    public synchronized V getOrDefault(UUID playerId, Supplier<V> def) {
        return get(playerId).orElseGet(def);
    }

    public synchronized V getOrDefault(EntityPlayer player, Supplier<V> def) throws RemoteException {
        return getOrDefault(player.getUniqueId(), def);
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

    public synchronized V getOrPut(EntityPlayer player, Supplier<V> factory) throws RemoteException {
        return getOrPut(player.getUniqueId(), factory);
    }
}
