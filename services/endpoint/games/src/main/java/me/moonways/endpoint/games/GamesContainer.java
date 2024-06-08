package me.moonways.endpoint.games;

import lombok.Synchronized;
import me.moonways.bridgenet.model.service.games.Game;
import me.moonways.endpoint.games.stub.GameStub;

import java.util.*;
import java.util.stream.Collectors;

public final class GamesContainer {

    private final Map<UUID, GameStub> games = Collections.synchronizedMap(new HashMap<>());

    @Synchronized
    public void addGame(GameStub game) {
        games.put(game.getUniqueId(), game);
    }

    @Synchronized
    public GameStub getGame(UUID uniqueId) {
        return games.get(uniqueId);
    }

    @Synchronized
    public GameStub getGameByName(String name) {
        return games.values()
                .stream()
                .filter(gameStub -> gameStub.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Synchronized
    public void deleteGame(UUID uniqueId) {
        games.remove(uniqueId);
    }

    @Synchronized
    public List<Game> getCollectedGames() {
        return games.values().stream().map(gameStub -> (Game) gameStub)
                .collect(Collectors.toList());
    }
}
