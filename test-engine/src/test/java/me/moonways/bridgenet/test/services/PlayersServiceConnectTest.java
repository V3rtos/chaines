package me.moonways.bridgenet.test.services;

import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessRemoteModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import me.moonways.bridgenet.model.players.PlayersServiceModel;
import me.moonways.bridgenet.model.players.connection.ConnectedEntityPlayer;
import me.moonways.bridgenet.model.players.connection.PlayerConnection;
import me.moonways.bridgenet.model.players.leveling.PlayerLeveling;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(BridgenetJUnitTestRunner.class)
public class PlayersServiceConnectTest {

    private AccessRemoteModule subj;

    @Before
    public void setUp() {
        ServiceInfo serviceInfo = new ServiceInfo("players", 7003, PlayersServiceModel.class);

        subj = new AccessRemoteModule();
        subj.init(serviceInfo, new AccessConfig("127.0.0.1"));
    }

    @Test
    public void test_successPlayerAdd() {
        PlayersServiceModel stub = subj.lookupStub();

        try {
            PlayerConnection playerConnection = stub.getPlayerConnection();
            playerConnection.addConnectedPlayer(
                    new ConnectedEntityPlayer(UUID.randomUUID(), "itzstonlex", null, null)
            );

            ConnectedEntityPlayer connectedPlayer = playerConnection.getConnectedPlayer("itzstonlex");

            assertEquals(connectedPlayer.getName(), "itzstonlex");
        }
        catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void test_successPlayerLeveling() {
        PlayersServiceModel stub = subj.lookupStub();

        try {
            PlayerLeveling playerLeveling = stub.getPlayerLeveling();

            int secondLevelExp = playerLeveling.calculateTotalExperience(2);

            assertEquals(10000, secondLevelExp);
            assertEquals(2, playerLeveling.calculateLevel(secondLevelExp));
            assertEquals(2, playerLeveling.calculateLevel(secondLevelExp + 1000));
            assertEquals(12500, playerLeveling.calculateExperienceToNextLevel(2));
            assertEquals(0, playerLeveling.calculateExperiencePercentToNextLevel(secondLevelExp + 5000));
        }
        catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }
}
