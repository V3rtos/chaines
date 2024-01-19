package me.moonways.endpoint.players.connection;

import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.bridgenet.model.players.connection.ConnectedEntityPlayer;
import me.moonways.bridgenet.model.players.connection.PlayerConnection;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerConnectionStub extends AbstractEndpointDefinition implements PlayerConnection {

    private static final long serialVersionUID = 2714012461490003142L;

    private final Map<UUID, ConnectedEntityPlayer> connectedPlayerByIdMap = new HashMap<>();

    public PlayerConnectionStub() throws RemoteException {
        super();
    }

    private void validateNull(ConnectedEntityPlayer connectedPlayer) {
        if (connectedPlayer == null) {
            throw new NullPointerException("connected player");
        }
    }

    @Override
    public void addConnectedPlayer(@NotNull ConnectedEntityPlayer connectedPlayer) {
        validateNull(connectedPlayer);
        connectedPlayerByIdMap.put(connectedPlayer.getUniqueId(), connectedPlayer);
    }

    @Override
    public void removeConnectedPlayer(@NotNull ConnectedEntityPlayer connectedPlayer) {
        validateNull(connectedPlayer);
        connectedPlayerByIdMap.remove(connectedPlayer.getUniqueId());
    }

    @Override
    public ConnectedEntityPlayer getConnectedPlayer(@NotNull UUID playerUUID) {
        return connectedPlayerByIdMap.get(playerUUID);
    }

    @Override
    public ConnectedEntityPlayer getConnectedPlayer(@NotNull String name) {
        return connectedPlayerByIdMap.values()
                .stream()
                .filter(connectedPlayer -> connectedPlayer.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
