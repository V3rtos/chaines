package me.moonways.endpoint.games;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.endpoint.games.stub.GameStub;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Autobind
public final class GamesContainer {

    private final Map<UUID, GameStub> games = Collections.synchronizedMap(new HashMap<>());

    public void registerGame() {

    }
}
