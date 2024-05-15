package me.moonways.bridgenet.test.connections.services;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.bus.message.CreateGame;
import me.moonways.bridgenet.model.bus.message.DeleteGame;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.model.bus.message.UpdateGame;
import me.moonways.bridgenet.model.games.*;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.data.management.ExampleNetworkConnection;
import me.moonways.bridgenet.test.data.junit.assertion.ServicesAssert;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.module.impl.RmiServicesModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import me.moonways.bridgenet.test.engine.persistance.TestOrdered;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertNull;

@RunWith(ModernTestEngineRunner.class)
@TestModules(RmiServicesModule.class)
public class GamesServiceEndpointTest {

    @Inject
    private ExampleNetworkConnection exampleNetworkConnection;
    @Inject
    private GamesServiceModel gamesServiceModel;

    private CreateGame.Result subj;

    @Test
    @TestOrdered(1)
    public void test_createGameSuccess() throws RemoteException {
        BridgenetNetworkChannel channel = exampleNetworkConnection.getChannel();
        sendHandshakeMessage();

        CompletableFuture<CreateGame.Result> future = channel.sendAwait(CreateGame.Result.class,
                new CreateGame(
                        TestConst.Game.NAME,
                        TestConst.Game.MAP, 2, 1));

        subj = future.join();

        Game game = gamesServiceModel.getGame(subj.getGameId());
        ServicesAssert.assertGame(game, subj, GameStatus.IDLE);
    }

    @Test
    @TestOrdered(2)
    public void test_updateGameState() throws RemoteException, InterruptedException {
        BridgenetNetworkChannel channel = exampleNetworkConnection.getChannel();
        channel.send(
                new UpdateGame(subj.getGameId(), subj.getActiveId(),
                        GameStatus.PROCESSING, 0, 0));

        sleep(100);

        Game game = gamesServiceModel.getGame(subj.getGameId());
        ServicesAssert.assertGame(game, subj, GameStatus.PROCESSING);
    }

    @Test
    @TestOrdered(3)
    public void test_successGameDelete() throws RemoteException, InterruptedException {
        BridgenetNetworkChannel channel = exampleNetworkConnection.getChannel();
        channel.send(
                new DeleteGame(subj.getGameId(), subj.getActiveId()));

        sleep(100);

        assertNull(gamesServiceModel.getGame(subj.getGameId()));
    }

    private Handshake newHandshakeMessage(@SuppressWarnings("SameParameterValue") String name) {
        Properties properties = new Properties();
        properties.setProperty("server.name", name);
        properties.setProperty("server.address.host", "127.0.0.1");
        properties.setProperty("server.address.port", "9005");
        return new Handshake(Handshake.Type.SERVER, properties);
    }

    private Handshake.Result sendHandshakeMessage() {
        BridgenetNetworkChannel channel = exampleNetworkConnection.getChannel();

        Handshake message = newHandshakeMessage("Test-1");
        CompletableFuture<Handshake.Result> future = channel.sendAwait(Handshake.Result.class, message);
        try {
            return future.join();
        } catch (CompletionException exception) {
            return new Handshake.Failure(UUID.randomUUID());
        }
    }
}
