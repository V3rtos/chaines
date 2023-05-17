package me.moonways.bridgenet.service.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import me.moonways.bridgenet.service.game.data.Arena;

@Getter
@RequiredArgsConstructor
public class GameArena {

    @Getter
    @Delegate
    private final Arena arena;

    @Getter
    private int totalPlayersCount;

    @Getter
    private int totalSpectatorsCount;

    public synchronized void addPlayer() {
        totalPlayersCount++;
    }

    public synchronized void removePlayer() {
        totalPlayersCount--;
    }

    public synchronized void addSpectator() {
        totalSpectatorsCount++;
    }

    public synchronized void removeSpectator() {
        totalSpectatorsCount--;
    }

    public synchronized void updatePlayersCount(int newPlayersCount) {
        this.totalPlayersCount = newPlayersCount;
    }

    public synchronized void updateSpectatorsCount(int newSpectatorsCount) {
        this.totalSpectatorsCount = newSpectatorsCount;
    }
}
