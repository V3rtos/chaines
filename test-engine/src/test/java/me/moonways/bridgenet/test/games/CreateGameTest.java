package me.moonways.bridgenet.test.games;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.bus.message.CreateGame;
import me.moonways.bridgenet.model.bus.message.DeleteGame;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.model.bus.message.UpdateGame;
import me.moonways.bridgenet.model.games.*;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import me.moonways.bridgenet.test.engine.util.TestMTPClientConnection;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(BridgenetJUnitTestRunner.class)
public class CreateGameTest {

    private static final String DEF_GAME_NAME = "Эмоциональные качели";
    private static final String DEF_GAME_MAP = "Детская площадка с программистом";

    @Inject
    private TestMTPClientConnection clientConnection;
    @Inject
    private GamesServiceModel gamesServiceModel;

    private CreateGame.Result subj;

    private Handshake newHandshakeMessage(@SuppressWarnings("SameParameterValue") String name) {
        Properties properties = new Properties();
        properties.setProperty("server.name", name);
        properties.setProperty("server.address.host", "127.0.0.1");
        properties.setProperty("server.address.port", "9005");
        return new Handshake(Handshake.Type.SERVER, properties);
    }

    private Handshake.Result sendHandshakeMessage() {
        BridgenetNetworkChannel channel = clientConnection.getChannel();

        Handshake message = newHandshakeMessage("Test-1");
        CompletableFuture<Handshake.Result> future = channel.sendAwait(Handshake.Result.class, message);
        try {
            return future.join();
        } catch (CompletionException exception) {
            return new Handshake.Failure(UUID.randomUUID());
        }
    }

    @Test
    public void test_createGameSuccess() throws RemoteException {
        BridgenetNetworkChannel channel = clientConnection.getChannel();
        sendHandshakeMessage();

        CompletableFuture<CreateGame.Result> future = channel.sendAwait(CreateGame.Result.class,
                new CreateGame(DEF_GAME_NAME, DEF_GAME_MAP, 2, 1));

        subj = future.join();

        assertServiceData(GameStatus.IDLE);
    }

    @Test
    public void test_updateGameState() throws RemoteException, InterruptedException {
        BridgenetNetworkChannel channel = clientConnection.getChannel();
        channel.send(
                new UpdateGame(subj.getGameId(), subj.getActiveId(),
                        GameStatus.PROCESSING, 0, 0));

        sleep(100);

        assertServiceData(GameStatus.PROCESSING);
    }

    @Test
    public void test_successGameDelete() throws RemoteException, InterruptedException {
        BridgenetNetworkChannel channel = clientConnection.getChannel();
        channel.send(
                new DeleteGame(subj.getGameId(), subj.getActiveId()));

        sleep(100);

        assertNull(gamesServiceModel.getGame(subj.getGameId()));
    }

    private void assertServiceData(GameStatus gameStatus) throws RemoteException {
        Game game = gamesServiceModel.getGame(subj.getGameId());

        ActiveGame activeGame = game.getActiveGame(subj.getActiveId()).get();
        GameState state = activeGame.getState();

        assertEquals(game.getName(), DEF_GAME_NAME);
        assertEquals(activeGame.getMap(), DEF_GAME_MAP);

        assertEquals(state.getMaxPlayers(), 2);
        assertEquals(state.getPlayersInTeam(), 1);
        assertEquals(state.getPlayers(), 0);
        assertEquals(state.getSpectators(), 0);
        assertEquals(state.getStatus(), gameStatus);
    }
}
