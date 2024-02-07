package me.moonways.endpoint.games.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.model.bus.message.CreateGame;
import me.moonways.bridgenet.model.bus.message.DeleteGame;
import me.moonways.bridgenet.model.bus.message.UpdateGame;
import me.moonways.bridgenet.model.games.*;
import me.moonways.bridgenet.model.servers.EntityServer;
import me.moonways.bridgenet.mtp.message.InputMessageContext;
import me.moonways.bridgenet.mtp.message.persistence.MessageHandler;
import me.moonways.bridgenet.mtp.message.persistence.MessageTrigger;
import me.moonways.endpoint.games.GamesContainer;
import me.moonways.endpoint.games.GamesEndpointException;
import me.moonways.endpoint.games.stub.ActiveGameStub;
import me.moonways.endpoint.games.stub.GameServerStub;
import me.moonways.endpoint.games.stub.GameStateStub;
import me.moonways.endpoint.games.stub.GameStub;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@MessageHandler
@RequiredArgsConstructor
public class GamesInputMessageListener {

    private final GamesContainer container;

    @MessageTrigger
    public void handle(InputMessageContext<CreateGame> context) throws RemoteException {
        ActiveGame activeGame = addActiveGame(context);

        UUID activeId = activeGame.getUniqueId();
        Game parent = activeGame.getParent();

        log.info("Game §2{} §rhas registered new arena by id: §2{}", parent.getName(), activeId);

        context.answer(new CreateGame.Result(parent.getUniqueId(), activeId));
    }

    @MessageTrigger
    public void handle(DeleteGame message) throws RemoteException {
        Optional<ActiveGame> activeGameOptional = findActiveGame(message.getGameId(), message.getActiveId());
        if (activeGameOptional.isPresent()) {

            ActiveGame activeGame = activeGameOptional.get();
            GameStub game = (GameStub) activeGame.getParent();

            for (GameServer gameServer : game.getLoadedServers()) {
                GameServerStub gameServerStub = (GameServerStub) gameServer;

                // удаляем активную игру из игрового сервера.
                gameServerStub.removeActiveGame(activeGame);

                // если активных игр не осталось в данном сервере, то удаляем сервер
                if (gameServer.getActiveGames().isEmpty()) {
                    game.removeGameServer(gameServerStub);
                }
            }

            // если в игре не осталось игровых серверов, удаляем игру
            if (game.getLoadedServers().isEmpty()) {
                container.deleteGame(game.getUniqueId());
                log.info("Game §4'{}' §rwas deleted", game.getName());

            } else {
                log.info("Game §4'{}' §rhas deleted new arena by id: §4{}", game.getName(), activeGame.getUniqueId());
            }
        }
    }

    @MessageTrigger
    public void handle(UpdateGame message) throws RemoteException {
        Optional<ActiveGame> activeGameOptional = findActiveGame(message.getGameId(), message.getActiveId());
        if (activeGameOptional.isPresent()) {

            ActiveGameStub activeGame = (ActiveGameStub) activeGameOptional.get();
            activeGame.setState(
                    toGameState(activeGame, message)
            );
        }
    }

    private GameState toGameState(CreateGame message) {
        return GameStateStub.builder()
                .spectators(0)
                .players(0)
                .status(GameStatus.IDLE)
                .playersInTeam(message.getPlayersInTeam())
                .maxPlayers(message.getMaxPlayers())
                .map(message.getMap())
                .build();
    }

    private GameState toGameState(ActiveGameStub activeGame, UpdateGame message) {
        return ((GameStateStub) activeGame.getState()).toBuilder()
                .spectators(message.getSpectators())
                .players(message.getPlayers())
                .status(message.getStatus())
                .build();
    }

    private GameServerStub toGameServerStub(InputMessageContext<CreateGame> context) throws RemoteException {
        Optional<EntityServer> entityServerOptional = context.getChannel().getProperty(EntityServer.CHANNEL_PROPERTY);

        if (!entityServerOptional.isPresent()) {
            throw new GamesEndpointException("parent server of created game is`nt initialized");
        }

        EntityServer server = entityServerOptional.get();
        return new GameServerStub(server.getServerInfo());
    }

    private GameStub getGameStub(CreateGame message) {
        GameStub game = container.getGameByName(message.getName());
        if (game == null) {
            game = new GameStub(UUID.randomUUID(), message.getName());
            container.addGame(game);
        }
        return game;
    }

    private ActiveGame addActiveGame(InputMessageContext<CreateGame> context) throws RemoteException {
        GameStub gameStub = getGameStub(context.getMessage());
        GameServerStub gameServerStub = toGameServerStub(context);

        ActiveGame activeGame = new ActiveGameStub(UUID.randomUUID(), gameStub,
                toGameState(context.getMessage()));

        gameServerStub.addActiveGame(activeGame);
        gameStub.addGameServer(gameServerStub);

        return activeGame;
    }

    private Optional<ActiveGame> findActiveGame(UUID gameId, UUID activeId) throws RemoteException {
        GameStub game = container.getGame(gameId);
        if (game != null) {
            return game.getActiveGame(activeId);
        }
        return Optional.empty();
    }
}
