package me.moonways.bridgenet.test.connections.services;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.model.players.PlayersServiceModel;
import me.moonways.bridgenet.model.players.connection.ConnectedEntityPlayer;
import me.moonways.bridgenet.model.players.connection.PlayerConnection;
import me.moonways.bridgenet.model.players.leveling.PlayerLeveling;
import me.moonways.bridgenet.test.engine.module.impl.RmiServicesModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import me.moonways.bridgenet.test.engine.persistance.TestOrdered;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(ModernTestEngineRunner.class)
@TestModules(RmiServicesModule.class)
public class PlayersServiceEndpointTest {

    @Inject
    private PlayersServiceModel serviceModel;

    @Test
    @TestOrdered(0)
    public void test_successPlayerAdd() throws RemoteException {
        PlayerConnection playerConnection = serviceModel.getPlayerConnection();
        playerConnection.addConnectedPlayer(
                new ConnectedEntityPlayer(UUID.randomUUID(), TestConst.Player.NICKNAME, null, null)
        );

        ConnectedEntityPlayer connectedPlayer = playerConnection.getConnectedPlayer(TestConst.Player.NICKNAME);

        assertEquals(connectedPlayer.getName(), TestConst.Player.NICKNAME);
    }

    @Test
    @TestOrdered(1)
    public void test_successPlayerLeveling() throws RemoteException {
        PlayerLeveling playerLeveling = serviceModel.getPlayerLeveling();

        int secondLevelExp = playerLeveling.calculateTotalExperience(2);

        assertEquals(10000, secondLevelExp);
        assertEquals(2, playerLeveling.calculateLevel(secondLevelExp));
        assertEquals(2, playerLeveling.calculateLevel(secondLevelExp + 1000));
        assertEquals(12500, playerLeveling.calculateExperienceToNextLevel(2));
        assertEquals(0, playerLeveling.calculateExperiencePercentToNextLevel(secondLevelExp + 5000));
    }
}
