package me.moonways.bridgenet.connector;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.connector.description.ActiveGameDescription;
import me.moonways.bridgenet.connector.description.GameDescription;
import me.moonways.bridgenet.connector.description.GameStateDescription;
import me.moonways.bridgenet.model.message.CreateGame;
import me.moonways.bridgenet.model.service.games.GameStatus;

import java.util.function.Function;

@RequiredArgsConstructor
public final class BridgenetGamesSync {

    private final BridgenetServerSync bridgenet;

    @Getter
    private ActiveGameDescription currentActive;
    @Getter
    private GameStateDescription currentState;

    /**
     * Исполнить инициализацию игры как активной и
     * отправить необходимые данные для ее существования
     * на сеть единого сервера Bridgenet.
     *
     * @param description - описание игры.
     */
    public synchronized void gameCreate(GameDescription description) {
        if (currentActive != null) {
            throw new IllegalStateException("game is already created");
        }

        CreateGame.Result exportedResult = bridgenet.exportGameCreate(description);
        gameActive(exportedResult);

        setDefaultState();
    }

    /**
     * Перевести состояние текущих кешей в активную
     * игру, которая была только что создана.
     *
     * @param exportingResult - результат экспортирования данных об игре.
     */
    private void gameActive(CreateGame.Result exportingResult) {
        currentActive = ActiveGameDescription.builder()
                .gameId(exportingResult.getGameId())
                .activeId(exportingResult.getActiveId())
                .build();
    }

    /**
     * Инициализировать стандартное состояние активной игры,
     * которая только что была создана.
     */
    private void setDefaultState() {
        currentState = GameStateDescription.builder()
                .activeGame(currentActive)
                .status(GameStatus.IDLE)
                .build();
    }

    /**
     * Обновить данные игры локально и на стороне
     * сети единого сервера Bridgenet.
     *
     * @param stateUpdater - функция обновления подлежащих для изменения параметров игры.
     */
    public synchronized void gameUpdate(Function<GameStateDescription, GameStateDescription> stateUpdater) {
        if (currentActive == null) {
            throw new IllegalStateException("game is not created");
        }

        GameStateDescription updated = stateUpdater.apply(currentState);

        if (updated.getActiveGame() == null || !updated.getActiveGame().equals(currentActive)) {
            throw new IllegalArgumentException("activeGame");
        }

        this.currentState = updated;
        bridgenet.exportGameUpdate(updated);
    }

    /**
     * Исполнить удаление и финализацию параметров
     * текущей сохраненной активной игры, а также
     * экспортировать данные об удалении на сеть
     * единого сервера Bridgenet.
     */
    public synchronized void gameDelete() {
        if (currentActive == null) {
            throw new IllegalStateException("game is not created");
        }

        bridgenet.exportGameDelete(currentActive);
        finalizeGame();
    }

    /**
     * Финализировать локальные кеши
     * текущей активной игры.
     */
    private void finalizeGame() {
        currentState = null;
        currentActive = null;
    }
}
