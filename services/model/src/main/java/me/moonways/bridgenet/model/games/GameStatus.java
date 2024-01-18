package me.moonways.bridgenet.model.games;

public enum GameStatus {

    IDLE,
    WAIT_PLAYERS,
    PROCESSING,
    INACTIVE,
    ;

    /**
     * Проверить может ли новый игрок присоединиться
     * к игре с текущим игровым статусом.
     */
    public boolean canNewPlayerJoin() {
        return this == IDLE || this == WAIT_PLAYERS;
    }
}
