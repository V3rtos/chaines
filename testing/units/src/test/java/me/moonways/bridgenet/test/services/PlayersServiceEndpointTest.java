package me.moonways.bridgenet.test.services;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.service.players.OfflinePlayer;
import me.moonways.bridgenet.model.service.players.Player;
import me.moonways.bridgenet.model.service.players.PlayersServiceModel;
import me.moonways.bridgenet.model.service.players.component.PlayerLeveling;
import me.moonways.bridgenet.model.service.players.component.PlayerStore;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.module.impl.RmiServicesModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import me.moonways.bridgenet.test.engine.persistance.TestOrdered;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(ModernTestEngineRunner.class)
@TestModules(RmiServicesModule.class)
public class PlayersServiceEndpointTest {

    @Inject
    private PlayersServiceModel serviceModel;

    @Test
    @TestOrdered(1)
    public void test_playersLevelingMath() throws RemoteException {
        PlayerLeveling playerLeveling = serviceModel.leveling();

        int level2_experience = playerLeveling.totalExperience(2);

        assertEquals(10000, level2_experience);
        assertEquals(2, playerLeveling.toLevel(level2_experience));
        assertEquals(2, playerLeveling.toLevel(level2_experience + 1000));
        assertEquals(12500, playerLeveling.experienceToNextLevel(2));
        assertEquals(0, playerLeveling.experiencePercentToNextLevel(level2_experience + 5000));
    }

    @Test
    @TestOrdered(2)
    public void test_onlinePlayer() throws RemoteException {
        PlayerStore store = serviceModel.store();
        Optional<Player> player = store.get(TestConst.Player.NICKNAME);

        assertTrue(player.isPresent());
    }

    @Test
    @TestOrdered(3)
    public void test_offlinePlayer() throws RemoteException {
        PlayerStore store = serviceModel.store();
        OfflinePlayer offlinePlayer = store.getOffline(TestConst.Player.NICKNAME);

        assertTrue(offlinePlayer.isOnline());

        assertTrue(offlinePlayer.getGroup().isDefault());
        assertTrue(offlinePlayer.getPermissions().isEmpty());

        assertEquals(offlinePlayer.getId(), TestConst.Player.ID);

        assertEquals(1, offlinePlayer.getLevel());
        assertEquals(0, offlinePlayer.getTotalExperience());
    }
}
