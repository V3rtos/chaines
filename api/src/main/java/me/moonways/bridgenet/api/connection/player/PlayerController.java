package me.moonways.bridgenet.api.connection.player;

import lombok.Getter;
import me.moonways.bridgenet.service.inject.Component;
import me.moonways.bridgenet.service.inject.InitMethod;
import me.moonways.bridgenet.service.inject.Inject;
import me.moonways.bridgenet.services.connection.player.OfflinePlayerRepository;
import net.conveno.jdbc.ConvenoRouter;
import net.conveno.jdbc.response.ConvenoResponse;
import net.conveno.jdbc.response.ConvenoResponseLine;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public final class PlayerController {

    private static final int PLAYER_ID_LABEL_INDEX = 1;
    private static final int PLAYER_NAME_LABEL_INDEX = 2;

    private final Map<Integer, ConnectedPlayer> connectedPlayerByIdMap = Collections.synchronizedMap(new HashMap<>());

    @Getter
    private OfflinePlayerRepository offlinePlayerRepository;

    @Inject
    private ConvenoRouter convenoRouter;

    @InitMethod
    private void init() {
        offlinePlayerRepository = convenoRouter.getRepository(OfflinePlayerRepository.class);
        offlinePlayerRepository.validateTableExists();
    }

    private ConvenoResponseLine getResponseLineByPlayerId(int playerId) {
        ConvenoResponse offlinePlayerResponse = offlinePlayerRepository.getOfflinePlayerByID(playerId);
        ConvenoResponseLine first = offlinePlayerResponse.first();

        if (first == null) {
            throw new NullPointerException("player response by id");
        }

        return first;
    }

    private ConvenoResponseLine getResponseLineByPlayerName(String playerName) {
        ConvenoResponse offlinePlayerResponse = offlinePlayerRepository.getOfflinePlayerByName(playerName);
        ConvenoResponseLine first = offlinePlayerResponse.first();

        if (first == null) {
            throw new NullPointerException("player response by name");
        }

        return first;
    }

    public OfflinePlayer getOfflinePlayer(int playerId) {
        ConvenoResponseLine responseLine = getResponseLineByPlayerId(playerId);
        return new OfflinePlayer(playerId,
                responseLine.getNullableString(PLAYER_NAME_LABEL_INDEX));
    }

    public OfflinePlayer getOfflinePlayer(@NotNull String playerName) {
        ConvenoResponseLine responseLine = getResponseLineByPlayerName(playerName);
        return new OfflinePlayer(
                responseLine.getNullableInt(PLAYER_ID_LABEL_INDEX),
                responseLine.getNullableString(PLAYER_NAME_LABEL_INDEX));
    }

    private void validateNull(ConnectedPlayer connectedPlayer) {
        if (connectedPlayer == null) {
            throw new NullPointerException("connected player");
        }
    }

    private void validateNull(String playerName) {
        if (playerName == null) {
            throw new NullPointerException("connected player name");
        }
    }

    public void addConnectedPlayer(@NotNull ConnectedPlayer connectedPlayer) {
        validateNull(connectedPlayer);
        connectedPlayerByIdMap.put(connectedPlayer.getPlayerId(), connectedPlayer);
    }

    public void removeConnectedPlayer(@NotNull ConnectedPlayer connectedPlayer) {
        validateNull(connectedPlayer);
        connectedPlayerByIdMap.remove(connectedPlayer.getPlayerId());
    }

    public String findPlayerName(int playerId) {
        ConvenoResponseLine responseLine = getResponseLineByPlayerId(playerId);
        return responseLine.getNullableString(PLAYER_NAME_LABEL_INDEX);
    }

    public int findPlayerId(@NotNull String playerName) {
        ConvenoResponseLine responseLine = getResponseLineByPlayerName(playerName);
        return responseLine.getNullableInt(PLAYER_ID_LABEL_INDEX);
    }

    public ConnectedPlayer getConnectedPlayer(int playerId) {
        return connectedPlayerByIdMap.get(playerId);
    }

    public ConnectedPlayer getConnectedPlayer(@NotNull String name) {
        return connectedPlayerByIdMap.values()
                .stream()
                .filter(connectedPlayer -> connectedPlayer.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
