package me.moonways.bridgenet.test.services;

import me.moonways.bridgenet.model.games.*;
import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessRemoteModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(BridgenetJUnitTestRunner.class)
public class GamesServiceConnectTest {

    private AccessRemoteModule subj;

    @Before
    public void setUp() {
        ServiceInfo serviceInfo = new ServiceInfo("games", 7005, GamesServiceModel.class);

        subj = new AccessRemoteModule();
        subj.init(serviceInfo, new AccessConfig("127.0.0.1"));
    }

    @Test
    public void test_success() {
        GamesServiceModel stub = subj.lookupStub();

        try {
            Game skywarsGame = stub.getGame("skywars");
            List<GameServer> japanMapServersList = skywarsGame.getLoadedServersByMap("Japan");

            for (GameServer japanMapServer : japanMapServersList) {
                List<ActiveGame> activeGames = japanMapServer.getActiveGames();

                for (ActiveGame activeGame : activeGames) {

                    assertTrue(activeGame.getState().checkStatus(GameStatus.IDLE));
                    assertEquals(activeGame.getMap(), "Japan");
                }
            }
        }
        catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }
}
